package org.hisp.india.trackercapture.domains.main;

import android.util.Log;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.androidannotations.annotations.App;
import org.hisp.india.core.services.schedulers.RxScheduler;
import org.hisp.india.trackercapture.models.base.DataValue;
import org.hisp.india.trackercapture.models.base.Enrollment;
import org.hisp.india.trackercapture.models.base.Event;
import org.hisp.india.trackercapture.models.base.OrganizationUnit;
import org.hisp.india.trackercapture.models.base.TrackedEntityInstance;
import org.hisp.india.trackercapture.models.e_num.ProgramStatus;
import org.hisp.india.trackercapture.models.e_num.SyncKey;
import org.hisp.india.trackercapture.models.response.EventsResponse;
import org.hisp.india.trackercapture.models.response.HeaderResponse;
import org.hisp.india.trackercapture.models.response.OrganizationUnitsResponse;
import org.hisp.india.trackercapture.models.response.QueryResponse;
import org.hisp.india.trackercapture.models.storage.RMapping;
import org.hisp.india.trackercapture.models.storage.ROrganizationUnit;
import org.hisp.india.trackercapture.models.storage.RProgram;
import org.hisp.india.trackercapture.models.storage.RProgramTrackedEntityAttribute;
import org.hisp.india.trackercapture.models.storage.RSync;
import org.hisp.india.trackercapture.models.storage.RTaskAttribute;
import org.hisp.india.trackercapture.models.storage.RTaskDataValue;
import org.hisp.india.trackercapture.models.storage.RTaskEnrollment;
import org.hisp.india.trackercapture.models.storage.RTaskEvent;
import org.hisp.india.trackercapture.models.storage.RTaskRequest;
import org.hisp.india.trackercapture.models.storage.RTaskTrackedEntityInstance;
import org.hisp.india.trackercapture.models.storage.RTrackedEntityInstance;
import org.hisp.india.trackercapture.models.storage.RUser;
import org.hisp.india.trackercapture.models.tmp.TMEnrollProgram;
import org.hisp.india.trackercapture.navigator.Screens;
import org.hisp.india.trackercapture.services.account.AccountQuery;
import org.hisp.india.trackercapture.services.account.AccountService;
import org.hisp.india.trackercapture.services.enrollments.EnrollmentService;
import org.hisp.india.trackercapture.services.filter.AuthenticationSuccessFilter;
import org.hisp.india.trackercapture.services.organization.OrganizationQuery;
import org.hisp.india.trackercapture.services.organization.OrganizationService;
import org.hisp.india.trackercapture.services.sync.SyncQuery;
import org.hisp.india.trackercapture.services.tracked_entity_instances.DefaultTrackedEntityInstanceService;
import org.hisp.india.trackercapture.services.tracked_entity_instances.TrackedEntityInstanceQuery;
import org.hisp.india.trackercapture.services.tracked_entity_instances.TrackedEntityInstanceService;
import org.hisp.india.trackercapture.utils.AppUtils;
import org.hisp.india.trackercapture.utils.RealmHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.realm.RealmList;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.R.attr.action;
import static android.R.attr.tabStripEnabled;

/**
 * Created by nhancao on 5/5/17.
 */

public class MainPresenter extends MvpBasePresenter<MainView> {
    private static final String TAG = MainPresenter.class.getSimpleName();

    @Inject
    protected Router router;
    @Inject
    protected NavigatorHolder navigatorHolder;
    @Inject
    protected OrganizationService organizationService;
    @Inject
    protected AccountService accountService;
    @Inject
    protected TrackedEntityInstanceService trackedEntityInstanceService;
    @Inject
    protected EnrollmentService enrollmentService;

    private Subscription subscription;
    private int orgUnitTotalPages;

    private List<RTrackedEntityInstance> trackedEntityInstances;

    private RTrackedEntityInstance selectedTrackedEntityInstance;

    @Inject
    public MainPresenter(Router router, NavigatorHolder navigatorHolder, AccountService accountService,
                         TrackedEntityInstanceService trackedEntityInstanceService) {
        this.router = router;
        this.navigatorHolder = navigatorHolder;
        this.accountService = accountService;
        this.trackedEntityInstanceService = trackedEntityInstanceService;
    }

    @Override
    public void attachView(MainView view) {
        super.attachView(view);
        navigatorHolder.setNavigator(view.getNavigator());
    }

    @Override
    public void detachView(boolean retainInstance) {
        navigatorHolder.removeNavigator();
        super.detachView(retainInstance);
    }

    public RUser getUserInfo() {
        return AccountQuery.getUser();
    }

    public void logout() {
        accountService.logout();
        router.replaceScreen(Screens.LOGIN_SCREEN);

    }

    public void getUserOrganizations() {
        List<ROrganizationUnit> tOrganizationUnits = OrganizationQuery.getUserOrganizations();
        getView().showOrgList(tOrganizationUnits);
        if (tOrganizationUnits.size() > 0) {
            getView().showProgramList(tOrganizationUnits.get(0).getPrograms());
        }
    }

    public void queryPrograms(String orgUnitId, String programId, ProgramStatus programStatus) {
        RxScheduler.onStop(subscription);
        getView().showLoading();
        subscription = trackedEntityInstanceService
                .queryTrackedEntityInstances(orgUnitId, programId, programStatus)
                .compose(RxScheduler.applyIoSchedulers())
                .doOnTerminate(() -> getView().hideLoading())
                .subscribe(queryResponse -> {
                    getView().queryProgramSuccess(queryResponse);
                });

    }

    //added by ifhaam
    public List<RTaskAttribute> getTaskAttributes(QueryResponse queryResponse,
                                                  RealmList<RProgramTrackedEntityAttribute> attributes
                                                    ,int trackedEntityInstance){
        List<RTaskAttribute> rTastAttributes = new ArrayList<>();

        for(RProgramTrackedEntityAttribute attribute : attributes){
            for(int i=0;i<queryResponse.getHeaders().size();i++){

                if(queryResponse.getHeaders().get(i) .getName().equals(attribute.getTrackedEntityAttribute().getId())){
                    RTaskAttribute rTaskAttribute = RTaskAttribute.create(attribute.getTrackedEntityAttribute().getId(),
                            queryResponse.getRows().get(trackedEntityInstance).get(i), attribute.getDisplayName()
                            ,attribute.getValueType().toString());
                    rTastAttributes.add(rTaskAttribute);
                    break;
                }
            }
        }

        return rTastAttributes;
    }



    //added by ifhaam on 28/09/2017
    public void getEvents(ROrganizationUnit orgUnit,String trackedInstanceId,QueryResponse queryResponse,
                          RProgram program,int trackedEntityInstance
            ){
        String orgUnitId = orgUnit.getId();
        RxScheduler.onStop(subscription);
        getView().showLoading("Fetching Stage Details");
        subscription = trackedEntityInstanceService
                .getEvents(orgUnitId,trackedInstanceId)
                .compose(RxScheduler.applyIoSchedulers())
                .doOnTerminate(()->getView().hideLoading())
                .doOnError((action)->{
                            getView().hideLoading();
                            getView().showError(action.getMessage());
                        }

                )
                .subscribe(eventsResponse -> {
                    //getView().getEventsSuccess(eventsResponse);
                    List<RTaskEvent> events = getRTastEvents(eventsResponse);
                    getTrakedEntityInstance(orgUnitId,trackedInstanceId,queryResponse,program,trackedEntityInstance,events);
                    Log.i(" TAG "," TEI Received");
              });
    }

    //ADDED BY IFHAAM ON 3/9/2017
    private TMEnrollProgram prepareTMEnrollProgram(QueryResponse queryResponse,
                                                   RProgram program,String orgUnitID,int trackedEntityInstance
                                                    ,List<RTaskEvent> events,String rTrackedEntityInstanceId,Enrollment enrollment){


        List<RTaskAttribute> rTaskAttributes  = getTaskAttributes(queryResponse,program.getProgramTrackedEntityAttributes(), trackedEntityInstance);
        String programID = program.getId();

        selectedTrackedEntityInstance = null;
        for(RTrackedEntityInstance rTrackedEntityInstance : trackedEntityInstances){
            if(rTrackedEntityInstance.getTrackedEntityInstanceId().equals(rTrackedEntityInstanceId)){
                selectedTrackedEntityInstance =rTrackedEntityInstance;
                break;
            }
        }

        RTaskTrackedEntityInstance rTaskTrackedEntityInstance = RTaskTrackedEntityInstance.create(
               selectedTrackedEntityInstance.getTrackedEntityId(),orgUnitID,rTaskAttributes,programID
        );
        rTaskTrackedEntityInstance.setTrackedEntityInstanceId(
                selectedTrackedEntityInstance.getTrackedEntityInstanceId());

        RTaskEnrollment rTaskEnrollment = RTaskEnrollment.create(programID,orgUnitID,
                AppUtils.trimTime(enrollment.getEnrollmentDate()).toString(),
                AppUtils.trimTime(enrollment.getIncidentDate()).toString());
        rTaskEnrollment.setEnrollmentId(enrollment.getEnrollment());
        rTaskEnrollment.setTrackedEntityInstanceId(
                selectedTrackedEntityInstance.getTrackedEntityInstanceId());


        RTaskRequest taskRequest = RTaskRequest.create(rTaskTrackedEntityInstance,
                rTaskEnrollment,events);

        TMEnrollProgram enrollProgram = new TMEnrollProgram(taskRequest);
        enrollProgram.getTaskRequest().setHadSynced(true);

        return  enrollProgram;

    }

    //added by ifhaam
    public List<RTaskEvent> getRTastEvents(EventsResponse eventsResponse){
        List<Event> events = eventsResponse.getEvents();
        List<RTaskEvent> rTaskEvents = new ArrayList<>();
        for(Event event:events){
            RTaskEvent rTaskEvent = new RTaskEvent();
            rTaskEvent.setDueDate(event.getDueDate());
            rTaskEvent.setEventDate(event.getEventDate());
            rTaskEvent.setEnrollmentId(event.getEnrollmentId());
            rTaskEvent.setOrgUnitId(event.getOrgUnitId());
            rTaskEvent.setProgramId(event.getProgramId());
            rTaskEvent.setProgramStageId(event.getProgramStageId());
            rTaskEvent.setStatus(event.getStatus());
            rTaskEvent.setTrackedEntityInstanceId(event.getTrackedEntityInstanceId());
            for(DataValue dataValue : event.getDataValues()){
                RTaskDataValue taskDataValue =
                        RTaskDataValue.create(dataValue.getValue(),
                                dataValue.getDataElementId(),
                                dataValue.isProvidedElsewhere());
                rTaskEvent.getDataValues().add(taskDataValue);

            }
            rTaskEvents.add(rTaskEvent);
        }
        return rTaskEvents;
    }

    //added by ifhaam
    public void registerProgram(TMEnrollProgram tmEnrollProgram, List<RTaskEvent> eventList) {
        RTaskRequest taskRequest = tmEnrollProgram.getTaskRequest();
        if (taskRequest == null) {
            taskRequest = RTaskRequest.create(tmEnrollProgram.getTaskRequest().getTrackedEntityInstance(),
                    tmEnrollProgram.getTaskRequest().getEnrollment(),
                    eventList);
        } else {
            taskRequest.setNeedSync(true);
            taskRequest.setEventList(eventList);
        }

        taskRequest.save();

        getView().registerProgramSyncRequest("Added into queue.");
        router.exit();
    }

    //added by ifhaam
    public void getTrakedEntityInstance(String orgId,String tei,QueryResponse queryResponse,
                                        RProgram program,int trackedEntityInstance
            ,List<RTaskEvent> events){
        RxScheduler.onStop(subscription);
        getView().showLoading(" Getting TEI data");
        subscription = (trackedEntityInstanceService)
                //.getTrackedEntituInstanceLocal(orgId, programId, tei)
                .getTrackedEntityInstances(orgId,program.getId())
                .compose(RxScheduler.applyIoSchedulers())
                .doOnTerminate(() -> {
                    Log.i(TAG," Succcccccccccccceeeeeeeeeed");
                    getView().hideLoading();
                    getEnrollment(tei,queryResponse,program,trackedEntityInstance,orgId,events);

                })
                .doOnError((Error)-> getView().hideLoading())
                .subscribe(trackedEntityInstances -> {
                    this.trackedEntityInstances = new ArrayList<>();
                    for(TrackedEntityInstance trackedEntityInstance1:trackedEntityInstances.getTrackedEntityInstances()){
                        this.trackedEntityInstances.add(RMapping.from(trackedEntityInstance1));
                    }

                });
    }

    public void getEnrollment(String trakedEntityInstanceId,QueryResponse queryResponse,
                              RProgram program,int trackedEntityInstance,String orgId,List<RTaskEvent> events){

        RxScheduler.onStop(subscription);
        subscription = enrollmentService
                .getEnrollments(trakedEntityInstanceId)
                .compose(RxScheduler.applyIoSchedulers())
                .doOnTerminate(()->{
                    Log.i(TAG," Enrollment Synced");
                    getView().hideLoading();
                }
                )
                .subscribe(enrollments->{
                       for(Enrollment enrollment:enrollments.getEnrollments()){
                           if(enrollment.getTrackedEntityInstance().equals(trakedEntityInstanceId)) {

                               TMEnrollProgram tmEnrollProgram = prepareTMEnrollProgram(queryResponse, program, orgId, trackedEntityInstance, events, trakedEntityInstanceId, enrollment);
                               registerProgram(tmEnrollProgram, events);
                               break;
                           }
                       }
                }

                );
    }

    public void fetchingAllOrgs() {
        RSync flagSync = SyncQuery.getSyncRowByKey(SyncKey.ROrganizationUnit);
        if (flagSync == null || !flagSync.isStatus()) {
            RxScheduler.onStop(subscription);
            List<ROrganizationUnit> currentOrgForUser = OrganizationQuery.getUserOrganizations();
            HashMap<String, Boolean> userOrgKeyMap = new HashMap<>();
            if (currentOrgForUser.size() > 0) {
                for (ROrganizationUnit rOrganizationUnit : currentOrgForUser) {
                    userOrgKeyMap.put(rOrganizationUnit.getId(), true);
                }
            }

            getView().showLoading("Retrieve all organization...");

            fetchOrganizationUnitsRecursion(1, userOrgKeyMap, organizationUnitsResponse -> {
                syncTrackedEntityInstance(currentOrgForUser, false);
            });

        } else {
            syncTrackedEntityInstance(OrganizationQuery.getUserOrganizations(), false);
        }
    }


    /**
     * Sync data
     */

    public void sync() {
        syncUserData();
    }

    public void syncUserData() {
        RxScheduler.onStop(subscription);
        getView().showLoading("Sync user data ...");
        subscription = accountService.login()
                .map(user -> {
                    getView().hideCircleProgressView();
                    getView().updateProgressStatus("Save user data ...");
                    return user;
                })
                .compose(
                        new AuthenticationSuccessFilter(accountService.getCredentials()).execute())
                .compose(RxScheduler.applyIoSchedulers())
                .doOnTerminate(() -> {
                    if (isViewAttached()) getView().hideLoading();
                })
                .subscribe(user -> {
                    System.gc();
                    syncOrganizationData();
                });
    }

    public void syncOrganizationData() {
        RxScheduler.onStop(subscription);

        //@nhancv TODO: create map for current org of user
        List<ROrganizationUnit> currentOrgForUser = OrganizationQuery.getUserOrganizations();
        HashMap<String, Boolean> userOrgKeyMap = new HashMap<>();
        if (currentOrgForUser.size() > 0) {
            for (ROrganizationUnit rOrganizationUnit : currentOrgForUser) {
                userOrgKeyMap.put(rOrganizationUnit.getId(), true);
            }
        }
        getView().showLoading("Retrieve all organization...");

        fetchOrganizationUnitsRecursion(1, userOrgKeyMap, organizationUnitsResponse -> {
            syncTrackedEntityInstance(currentOrgForUser, true);
        });
    }

    /*************************************************
     * For house hold program
     */
    public void syncTrackedEntityInstance(List<ROrganizationUnit> currentOrgForUser, boolean isForceSync) {
        RSync flagSync = SyncQuery.getSyncRowByKey(SyncKey.RTaskTrackedEntityInstance);
        if (isForceSync || flagSync == null || !flagSync.isStatus()) {
            getView().showLoading("Sync TEI...");
            rx.Observable.defer(() -> rx.Observable.from(currentOrgForUser)
                    .flatMap(rOrganizationUnit -> rx.Observable
                            .from(rOrganizationUnit.getPrograms())
                            .filter(program -> program.getDisplayName().equals("Household"))
                            .flatMap(program2 ->
                                    trackedEntityInstanceService
                                            .getTrackedEntityInstances(rOrganizationUnit.getId(), program2.getId())
                                            .flatMap(
                                                    trackedEntityInstancesResponse -> {
                                                        List<RTrackedEntityInstance> trackedEntityInstances = new ArrayList<>();
                                                        for (TrackedEntityInstance trackedEntityInstance : trackedEntityInstancesResponse.getTrackedEntityInstances()) {
                                                            trackedEntityInstances.add(RMapping.from(trackedEntityInstance)
                                                                    .setProgramId(program2.getId()));

                                                        }
                                                        TrackedEntityInstanceQuery.insertOrUpdate(trackedEntityInstances);
                                                        return rx.Observable.just(trackedEntityInstances);
                                                    })))
                    .map(rTrackedEntityInstances -> {
                        RealmHelper.transaction(realm -> {
                            //update sync flag
                            realm.copyToRealmOrUpdate(
                                    RSync.create(SyncKey.RTaskTrackedEntityInstance, true));
                        });
                        trackedEntityInstances = rTrackedEntityInstances;
                        return rTrackedEntityInstances;
                    }))
                    .compose(RxScheduler.applyIoSchedulers())
                    .doOnCompleted(() -> updateViewSynced(isForceSync))
                    .doOnError(throwable -> {
                        if (isViewAttached()) getView().showError(throwable.getMessage());
                    })
                    .subscribe();
        } else {
            updateViewSynced(false);
        }

    }

    private void updateViewSynced(boolean isForceSync) {
        if (isViewAttached()) {
            getUserOrganizations();
            getView().hideLoading();
            if (isForceSync) {
                getView().syncSuccessful();
            }
        }
    }

    public void fetchOrganizationUnitsRecursion(int page,
                                                HashMap<String, Boolean> userOrgKeyMap,
                                                Action1<? super OrganizationUnitsResponse> onNext) {
        String m = (orgUnitTotalPages <= 0) ? String.format("%s/%s", page, "-") :
                String.format("%s/%s", page, orgUnitTotalPages);
        getView().hideCircleProgressView();
        getView().updateProgressStatus("Retrieve organization (" + m + ")...");
        organizationService.getOrganizationUnits(page)
                .observeOn(Schedulers.io())
                .map(organizationUnitsResponse -> {
                    orgUnitTotalPages = organizationUnitsResponse.getPageResponse().getPageCount();
                    if (getView() != null) {
                        getView().hideCircleProgressView();
                        getView().updateProgressStatus(
                                String.format("Saving organizations (%s/%s)...", page, orgUnitTotalPages));
                    }

                    RealmHelper.transaction(realm -> {
                        for (OrganizationUnit organizationUnit :
                                organizationUnitsResponse.getOrganizationUnits()) {
                            if (!userOrgKeyMap.containsKey(organizationUnit.getId())) {
                                realm.copyToRealmOrUpdate(RMapping.from(organizationUnit));
                            }
                        }

                        if (page >= orgUnitTotalPages) {
                            //update sync flag
                            realm.copyToRealmOrUpdate(
                                    RSync.create(SyncKey.ROrganizationUnit, true));
                        }
                    });

                    return organizationUnitsResponse;
                })
                .compose(RxScheduler.applyIoSchedulers())
                .subscribe(organizationUnitsResponse -> {
                    if (page < orgUnitTotalPages) {
                        fetchOrganizationUnitsRecursion(page + 1, userOrgKeyMap, onNext);
                    } else if (isViewAttached()) {
                        getView().hideLoading();
                        onNext.call(organizationUnitsResponse);
                    }
                }, throwable -> {
                    if (isViewAttached()) getView().showError(throwable.getMessage());
                });
    }

}
