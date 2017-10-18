package org.hisp.india.trackercapture.domains.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.SlidingRootNavLayout;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.Subscribe;
import org.hisp.india.core.bus.ProgressBus;
import org.hisp.india.core.services.network.DefaultNetworkProvider;
import org.hisp.india.trackercapture.MainApplication;
import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.domains.base.BaseActivity;
import org.hisp.india.trackercapture.domains.enroll_program.EnrollProgramActivity_;
import org.hisp.india.trackercapture.domains.enroll_program_stage.EnrollProgramStageActivity_;
import org.hisp.india.trackercapture.domains.login.LoginActivity_;
import org.hisp.india.trackercapture.domains.menu.DrawerAdapter;
import org.hisp.india.trackercapture.domains.menu.DrawerItem;
import org.hisp.india.trackercapture.domains.menu.MenuItem;
import org.hisp.india.trackercapture.domains.menu.SimpleItem;
import org.hisp.india.trackercapture.domains.menu.SpaceItem;
import org.hisp.india.trackercapture.domains.sync_queue.SyncQueueActivity_;
import org.hisp.india.trackercapture.domains.tracked_entity.TrackedEntityActivity_;
import org.hisp.india.trackercapture.models.base.Program;
import org.hisp.india.trackercapture.models.base.RowModel;
import org.hisp.india.trackercapture.models.response.EventsResponse;
import org.hisp.india.trackercapture.models.response.PageResponse;
import org.hisp.india.trackercapture.models.response.QueryResponse;
import org.hisp.india.trackercapture.models.storage.RAttribute;
import org.hisp.india.trackercapture.models.storage.ROrganizationUnit;
import org.hisp.india.trackercapture.models.storage.RProgram;
import org.hisp.india.trackercapture.models.storage.RProgramTrackedEntityAttribute;
import org.hisp.india.trackercapture.models.storage.RTaskEnrollment;
import org.hisp.india.trackercapture.models.storage.RTaskRequest;
import org.hisp.india.trackercapture.models.storage.RTrackedEntityInstance;
import org.hisp.india.trackercapture.models.storage.RUser;
import org.hisp.india.trackercapture.models.tmp.TMEnrollProgram;
import org.hisp.india.trackercapture.navigator.Screens;
import org.hisp.india.trackercapture.services.sync.AutoSyncService;
import org.hisp.india.trackercapture.services.sync.SyncBus;
import org.hisp.india.trackercapture.services.sync.SyncQuery;
import org.hisp.india.trackercapture.utils.AppUtils;
import org.hisp.india.trackercapture.utils.RealmHelper;
import org.hisp.india.trackercapture.widgets.autocomplete.AutocompleteDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import es.dmoral.toasty.Toasty;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.commands.Forward;
import ru.terrakok.cicerone.commands.Replace;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity<MainView, MainPresenter>
        implements MainView, DrawerAdapter.OnItemSelectedListener{
    private static final String TAG = MainActivity.class.getSimpleName();

    @ViewById(R.id.activity_main_toolbar)
    protected Toolbar toolbar;
    @ViewById(R.id.activity_main_tv_organization)
    protected TextView tvOrganization;
    @ViewById(R.id.activity_main_tv_program)
    protected TextView tvProgram;
    @ViewById(R.id.activity_main_bt_search)
    protected View btSearch;
    @ViewById(R.id.activity_main_bt_enroll)
    protected View btEnroll;
    @ViewById(R.id.activity_main_bt_search_local)
    protected View btSearchLocal;
    @ViewById(R.id.activity_main_lv_program)
    protected ListView lvProgram;
    @ViewById(R.id.item_program_info_tv_header_1)
    protected TextView tvHeader1;
    @ViewById(R.id.item_program_info_tv_header_2)
    protected TextView tvHeader2;
    @ViewById(R.id.item_program_info_tv_header_3)
    protected TextView tvHeader3;
    @ViewById(R.id.tv_next)
    protected TextView tvNext;
    @ViewById(R.id.tv_previous)
    protected TextView tvPrevious;
    @ViewById(R.id.tv_page_count)
    protected TextView tvPageCount;

    @App
    protected MainApplication application;
    @Inject
    protected MainPresenter presenter;

    private RProgram program;
    private ROrganizationUnit organizationUnit;

    private AutocompleteDialog dialog;
    private List<RProgram> programList;
    private List<ROrganizationUnit> organizationUnitList;

    private PageResponse pageResponse;

    private Navigator navigator = command -> {
        if (command instanceof Replace) {

            if (((Replace) command).getScreenKey().equals(Screens.LOGIN_SCREEN)) {
                LoginActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                        .start();
            }

        } else if (command instanceof Forward) {
            if (((Forward) command).getScreenKey().equals(Screens.ENROLL_PROGRAM)) {

                TMEnrollProgram tmEnrollProgram = new TMEnrollProgram(organizationUnit.getId(), program.getId());
                EnrollProgramActivity_.intent(this)
                        .tmEnrollProgramJson(TMEnrollProgram.toJson(tmEnrollProgram))
                        .start();

            } else if (((Forward) command).getScreenKey().equals(Screens.TRACKED_ENTITY)) {

                TrackedEntityActivity_.intent(this)
                        .rowModel((RowModel) ((Forward) command).getTransitionData())
                        .start();

            }else if (((Forward) command).getScreenKey().equals(Screens.ENROLL_PROGRAM_STAGE)) {

                RTaskRequest taskRequest = SyncQuery.getRTaskRequest((String) ((Forward) command).getTransitionData());
                RTaskEnrollment enrollment = taskRequest.getEnrollment();
                String orgUnitId = null, programId = null;
                if (enrollment != null) {
                    orgUnitId = enrollment.getOrgUnitId();
                    programId = enrollment.getProgramId();
                }

                if (orgUnitId != null && programId != null) {


                    EnrollProgramStageActivity_.intent(this)
                            .tmEnrollProgramJson(
                                    TMEnrollProgram
                                            .toJson(new TMEnrollProgram(taskRequest)))
                            .start();
                } else {
                    Toast.makeText(application, "TaskRequest info is null", Toast.LENGTH_SHORT).show();
                }

            }
        }
    };

    @AfterInject
    void inject() {
        DaggerMainComponent.builder()
                .applicationComponent(application.getApplicationComponent())
                .build()
                .inject(this);
    }

    @AfterViews
    void init() {

        //Making notification bar transparent
        AppUtils.changeStatusBarColor(this);
        //Setup toolbar
        setSupportActionBar(toolbar);

        //Built menu
        SlidingRootNavLayout navLayout = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withMenuLayout(R.layout.menu_drawer)
                .withContentClickableWhenMenuOpened(false)
                .inject()
                .getLayout();
        TextView tvCharacterName = (TextView) navLayout.findViewById(R.id.menu_drawer_tv_character_name);
        TextView tvDisplayName = (TextView) navLayout.findViewById(R.id.menu_drawer_tv_display_name);
        TextView tvEmail = (TextView) navLayout.findViewById(R.id.menu_drawer_tv_email);

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(MenuItem.SYNC),
                createItemFor(MenuItem.QUEUE),
                createItemFor(MenuItem.SETTINGS),
                createItemFor(MenuItem.INFO),
                new SpaceItem(48),
                createItemFor(MenuItem.LOGOUT)));

        adapter.setListener(this);

        RecyclerView list = (RecyclerView) findViewById(R.id.menu_drawer_rv_list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        //added by ifhaam
        initResponseContollers();

        //Update info
        RUser user = presenter.getUserInfo();
       String character = "";
        if (user != null) {
            if (!TextUtils.isEmpty(user.getFirstName()) && !TextUtils.isEmpty(user.getSurName())) {
                character = String.valueOf(user.getFirstName().charAt(0)) +
                        String.valueOf(user.getSurName().charAt(0));
            } else if (!TextUtils.isEmpty(user.getDisplayName()) && user.getDisplayName().length() > 1) {
                character = String.valueOf(user.getDisplayName().charAt(0)) +
                        String.valueOf(user.getDisplayName().charAt(1));
            }
            tvCharacterName.setText(character);
            tvDisplayName.setText(user.getDisplayName());
            tvEmail.setText(user.getEmail());
            updateBtSearch();

        } else {
            presenter.logout();
        }


    }
    //added by ifhaam
    private void initResponseContollers(){

        tvNext.setOnClickListener((event)->{
            if(pageResponse==null){
                Toasty.info(getApplicationContext(),"No Data").show();
            }else{
                if(pageResponse.getPage()<pageResponse.getPageCount()){
                    //presenter.queryPrograms(organizationUnit.getId(),program.getId(),ProgramStatus.ACTIVE,pageResponse.getPage()+1);
                    presenter.downloadInstances(organizationUnit,program,pageResponse.getPage()+1);
                }else{
                    presenter.downloadInstances(organizationUnit,program);
                    //presenter.queryPrograms(organizationUnit.getId(),program.getId(),ProgramStatus.ACTIVE);
                }
            }
        });

        tvPrevious.setOnClickListener((event)->{
            if(pageResponse==null){
                Toasty.info(getApplicationContext(),"No Data").show();
            }else{
                if(pageResponse.getPage()==1){
                    presenter.downloadInstances(organizationUnit,program);
                    //presenter.queryPrograms(organizationUnit.getId(),program.getId(),ProgramStatus.ACTIVE,pageResponse.getPageCount());
                }else{
                    presenter.downloadInstances(organizationUnit,program,pageResponse.getPage()-1);
                    //presenter.queryPrograms(organizationUnit.getId(),program.getId(),ProgramStatus.ACTIVE,pageResponse.getPage()-1);
                }
            }
        });
        String pageCountText = pageResponse==null?"N/A":pageResponse.getPage()+"/"+pageResponse.getPageCount();
        tvPageCount.setText(pageCountText);

    }

    @Override
    protected void onResume() {
        super.onResume();
        DefaultNetworkProvider.PROGRESS_BUS.register(this);
        AutoSyncService.bus.register(this);
        presenter.fetchingAllOrgs();
    }

    @Override
    protected void onPause() {
        DefaultNetworkProvider.PROGRESS_BUS.unregister(this);
        AutoSyncService.bus.unregister(this);
        super.onPause();
    }

    @Subscribe
    public void syncBusSubscribe(SyncBus syncBus) {
        switch (syncBus.getStatus()) {
            case ERROR:
                Toasty.error(this, syncBus.getMessage()).show();
                break;
            case SUCCESS:
                Toasty.info(this, "Register succeed").show();
                break;
        }
    }

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void onItemSelected(MenuItem menuItem) {
        switch (menuItem) {
            case SYNC:
                presenter.sync();
                break;
            case QUEUE:
                SyncQueueActivity_.intent(this).start();
                break;
            case LOGOUT:
                presenter.logout();
                RealmHelper.transaction((realm)->realm.deleteAll());

                break;
            default:
                break;
        }
    }

    @Override
    public Navigator getNavigator() {
        return navigator;
    }

    @Override
    public void showLoading() {
        showProgressLoading();
    }

    @Override
    public void showLoading(String msg) {
        showProgressLoading(msg);
    }

    @Override
    public void hideLoading() {
        hideProgressLoading();
    }

    @Override
    public void showOrgList(List<ROrganizationUnit> organizationUnitList) {
        this.organizationUnitList = organizationUnitList;
        tvOrganization.setEnabled(true);
    }

    @Override
    public void showProgramList(List<RProgram> programList) {
        this.programList = programList;
        tvProgram.setEnabled(true);
    }

    @Override
    public void queryProgramSuccess(QueryResponse queryResponse) {

    }

    //added by ifhaam
    @Override
    public void getEventsSuccess(EventsResponse eventsResponse){

        Toasty.info(getApplicationContext(),eventsResponse.toString()).show();
    }

    //added by ifhaam
    @Override
    public void registerProgramSyncRequest(String msg) {
        Toasty.info(this, msg).show();
        AutoSyncService.start(getApplicationContext());
    }

    //added by ifhaam
    public void downloadInstancesSuccess(QueryResponse queryResponse, HashMap<String,String> uuidList, List<RTrackedEntityInstance> trackedEntityInstances){
        List<String> displayInList = new ArrayList<String>();
        List<String> rowHeader = new ArrayList<>();

        for (RProgramTrackedEntityAttribute programTrackedEntityAttribute : program
                .getProgramTrackedEntityAttributes()) {
            if (displayInList.size() < 3 &&
                    programTrackedEntityAttribute.isDisplayInList()) {
                displayInList
                        .add(programTrackedEntityAttribute.getTrackedEntityAttribute().getId());
                rowHeader.add(programTrackedEntityAttribute.getTrackedEntityAttribute().getDisplayName());
            }
        }

        tvHeader1.setVisibility(View.GONE);
        tvHeader2.setVisibility(View.GONE);
        tvHeader3.setVisibility(View.GONE);
        if (rowHeader.size() > 0) {
            tvHeader1.setText(rowHeader.get(0));
            tvHeader1.setVisibility(View.VISIBLE);
        }
        if (rowHeader.size() > 1) {
            tvHeader2.setText(rowHeader.get(1));
            tvHeader2.setVisibility(View.VISIBLE);
        }
        if (rowHeader.size() > 2) {
            tvHeader3.setText(rowHeader.get(2));
            tvHeader3.setVisibility(View.VISIBLE);
        }


        lvProgram.setAdapter(new QuickAdapter<RTrackedEntityInstance>(this, R.layout.item_program_info, trackedEntityInstances) {
            @Override
            protected void convert(BaseAdapterHelper helper, RTrackedEntityInstance trackedEntityInstance) {
                helper.setVisible(R.id.item_program_info_tv_1, false);
                helper.setVisible(R.id.item_program_info_tv_2, false);
                helper.setVisible(R.id.item_program_info_tv_3, false);


                HashMap<String,String> valToDisplay = new HashMap<String, String>();

                for(String id :displayInList){
                    for(RAttribute attribute :trackedEntityInstance.getAttributeList()){

                        if(id.equals(attribute.getAttributeId())){
                            valToDisplay.put(attribute.getAttributeId(),attribute.getValue());
                            break;
                        }
                    }

                }

                if (rowHeader.size() > 0) {
                    String val = valToDisplay.get(displayInList.get(0));
                    if(val==null) val ="";
                    helper.setText(R.id.item_program_info_tv_1, val);
                    helper.setVisible(R.id.item_program_info_tv_1, true);
                }
                if (rowHeader.size() > 1) {
                    String val = valToDisplay.get(displayInList.get(1));
                    if(val==null) val ="";
                    helper.setText(R.id.item_program_info_tv_2, val);
                    helper.setVisible(R.id.item_program_info_tv_2, true);
                }
                if (rowHeader.size() > 2) {
                    String val = valToDisplay.get(displayInList.get(2));
                    if(val==null) val ="";
                    helper.setText(R.id.item_program_info_tv_3, val);
                    helper.setVisible(R.id.item_program_info_tv_3, true);
                }

                helper.getView().setOnClickListener(v -> {

                    /*removed by ifhaam

                    navigator.applyCommand(new Forward(Screens.TRACKED_ENTITY,
                            new RowModel(queryResponse.getHeaders(),
                                    queryResponse.getRows()
                                            .get(helper.getPosition()))));
                    */

                    //added by ifhaam for testing
                    /*presenter.getEvents(organizationUnit,//orgunit has to be fetched from row headers as well
                            //temperarily im using this.
                            getTrackedInstanceId(
                                    new RowModel(queryResponse.getHeaders()
                                    ,queryResponse.getRows()
                                    .get(helper.getPosition())))
                                    ,queryResponse,program,helper.getPosition()
                            );*/
                    /*String trackedEntityInstanceID = getTrackedInstanceId(
                            new RowModel(queryResponse.getHeaders()
                                    ,queryResponse.getRows()
                                    .get(helper.getPosition()))
                    );*/
                    presenter.editData(uuidList.get(trackedEntityInstance.getTrackedEntityInstanceId()));
                });




            }
        });
    }

    @Override
    public void downloadInstancesSuccess(HashMap<String,String> uuidList, List<RTrackedEntityInstance> trackedEntityInstances,PageResponse pageResponse){

        this.pageResponse =pageResponse;
        initResponseContollers();

        List<String> displayInList = new ArrayList<String>();
        List<String> rowHeader = new ArrayList<>();

        for (RProgramTrackedEntityAttribute programTrackedEntityAttribute : program
                .getProgramTrackedEntityAttributes()) {
            if (displayInList.size() < 3 &&
                    programTrackedEntityAttribute.isDisplayInList()) {
                displayInList
                        .add(programTrackedEntityAttribute.getTrackedEntityAttribute().getId());
                rowHeader.add(programTrackedEntityAttribute.getTrackedEntityAttribute().getDisplayName());
            }
        }

        tvHeader1.setVisibility(View.GONE);
        tvHeader2.setVisibility(View.GONE);
        tvHeader3.setVisibility(View.GONE);
        if (rowHeader.size() > 0) {
            tvHeader1.setText(rowHeader.get(0));
            tvHeader1.setVisibility(View.VISIBLE);
        }
        if (rowHeader.size() > 1) {
            tvHeader2.setText(rowHeader.get(1));
            tvHeader2.setVisibility(View.VISIBLE);
        }
        if (rowHeader.size() > 2) {
            tvHeader3.setText(rowHeader.get(2));
            tvHeader3.setVisibility(View.VISIBLE);
        }


        lvProgram.setAdapter(new QuickAdapter<RTrackedEntityInstance>(this, R.layout.item_program_info, trackedEntityInstances) {
            @Override
            protected void convert(BaseAdapterHelper helper, RTrackedEntityInstance trackedEntityInstance) {
                helper.setVisible(R.id.item_program_info_tv_1, false);
                helper.setVisible(R.id.item_program_info_tv_2, false);
                helper.setVisible(R.id.item_program_info_tv_3, false);


                HashMap<String,String> valToDisplay = new HashMap<String, String>();

                for(String id :displayInList){
                    for(RAttribute attribute :trackedEntityInstance.getAttributeList()){

                        if(id.equals(attribute.getAttributeId())){
                            valToDisplay.put(attribute.getAttributeId(),attribute.getValue());
                            break;
                        }
                    }

                }

                if (rowHeader.size() > 0) {
                    String val = valToDisplay.get(displayInList.get(0));
                    if(val==null) val ="";
                    helper.setText(R.id.item_program_info_tv_1, val);
                    helper.setVisible(R.id.item_program_info_tv_1, true);
                }
                if (rowHeader.size() > 1) {
                    String val = valToDisplay.get(displayInList.get(1));
                    if(val==null) val ="";
                    helper.setText(R.id.item_program_info_tv_2, val);
                    helper.setVisible(R.id.item_program_info_tv_2, true);
                }
                if (rowHeader.size() > 2) {
                    String val = valToDisplay.get(displayInList.get(2));
                    if(val==null) val ="";
                    helper.setText(R.id.item_program_info_tv_3, val);
                    helper.setVisible(R.id.item_program_info_tv_3, true);
                }

                helper.getView().setOnClickListener(v -> {
                    presenter.editData(uuidList.get(trackedEntityInstance.getTrackedEntityInstanceId()));
                });




            }
        });
    }

    @Override
    public void downloadInstancesSuccess(QueryResponse queryResponse,HashMap<String,String> uuidList){
        Map<String, Pair<Integer, String>> displayInList = new LinkedHashMap<>();
        for (RProgramTrackedEntityAttribute programTrackedEntityAttribute : program
                .getProgramTrackedEntityAttributes()) {
            if (displayInList.size() < 3 &&
                    programTrackedEntityAttribute.isDisplayInList()) {
                displayInList
                        .put(programTrackedEntityAttribute.getTrackedEntityAttribute().getId(), null);
            }
        }

        for (String key : displayInList.keySet()) {
            for (int i = 0; i < queryResponse.getHeaders().size(); i++) {
                if (key.equals(queryResponse.getHeaders().get(i).getName())) {
                    displayInList.put(key, new Pair<>(i, queryResponse.getHeaders().get(i).getColumn()));
                    break;
                }
            }
        }

        List<List<String>> listRows = queryResponse.getRows();
        List<List<String>> listData = new ArrayList<>();

        for (int i = 0; i < listRows.size(); i++) {
            List<String> row = new ArrayList<>();
            for (String key : displayInList.keySet()) {
                row.add(listRows.get(i).get(displayInList.get(key).first));
            }
            listData.add(row);
        }

        //Set header
        List<String> rowHeader = new ArrayList<>();
        for (Pair<Integer, String> integerStringPair : displayInList.values()) {
            rowHeader.add(integerStringPair.second);
        }

        tvHeader1.setVisibility(View.GONE);
        tvHeader2.setVisibility(View.GONE);
        tvHeader3.setVisibility(View.GONE);
        if (rowHeader.size() > 0) {
            tvHeader1.setText(rowHeader.get(0));
            tvHeader1.setVisibility(View.VISIBLE);
        }
        if (rowHeader.size() > 1) {
            tvHeader2.setText(rowHeader.get(1));
            tvHeader2.setVisibility(View.VISIBLE);
        }
        if (rowHeader.size() > 2) {
            tvHeader3.setText(rowHeader.get(2));
            tvHeader3.setVisibility(View.VISIBLE);
        }

        //Set list view of programs
        lvProgram.setAdapter(new QuickAdapter<List<String>>(this, R.layout.item_program_info, listData) {
            @Override
            protected void convert(BaseAdapterHelper helper, List<String> item) {
                helper.setVisible(R.id.item_program_info_tv_1, false);
                helper.setVisible(R.id.item_program_info_tv_2, false);
                helper.setVisible(R.id.item_program_info_tv_3, false);
                if (item.size() > 0) {
                    helper.setText(R.id.item_program_info_tv_1, item.get(0));
                    helper.setVisible(R.id.item_program_info_tv_1, true);
                }
                if (item.size() > 1) {
                    helper.setText(R.id.item_program_info_tv_2, item.get(1));
                    helper.setVisible(R.id.item_program_info_tv_2, true);
                }
                if (item.size() > 2) {
                    helper.setText(R.id.item_program_info_tv_3, item.get(2));
                    helper.setVisible(R.id.item_program_info_tv_3, true);
                }

                helper.getView().setOnClickListener(v -> {

                    /*removed by ifhaam

                    navigator.applyCommand(new Forward(Screens.TRACKED_ENTITY,
                            new RowModel(queryResponse.getHeaders(),
                                    queryResponse.getRows()
                                            .get(helper.getPosition()))));
                    */

                    //added by ifhaam for testing
                    /*presenter.getEvents(organizationUnit,//orgunit has to be fetched from row headers as well
                            //temperarily im using this.
                            getTrackedInstanceId(
                                    new RowModel(queryResponse.getHeaders()
                                    ,queryResponse.getRows()
                                    .get(helper.getPosition())))
                                    ,queryResponse,program,helper.getPosition()
                            );*/
                    String trackedEntityInstance = getTrackedInstanceId(
                            new RowModel(queryResponse.getHeaders()
                                    ,queryResponse.getRows()
                                    .get(helper.getPosition()))
                                    );
                    presenter.editData(uuidList.get(trackedEntityInstance));
                });




            }
        });
    }

    @Override
    public void queryProgramSuccess(QueryResponse queryResponse,int page) {
       // uuidList = new ArrayList<>();
        //presenter.downloadInstances(organizationUnit,program,queryResponse,page);
        pageResponse = queryResponse.getMetaData().getPager();
        initResponseContollers();
    }




    //added by ifhaam
    private String getTrackedInstanceId(RowModel rowModel){
        for(int i=0;i< rowModel.getHeaders().size();i++){
            if(rowModel.getHeaders().get(i).getName().equals("instance")){
                return rowModel.getRows().get(i);
            }
        }
        return "";
    }

    @Override
    public void syncSuccessful() {
        Toast.makeText(application, "Sync succeed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String error) {
        Toasty.error(this, error).show();
    }

    @Override
    public void updateProgressStatus(String message) {
        runOnUiThread(() -> updateProgressText(message));
    }

    @Override
    public void hideCircleProgressView() {
        runOnUiThread(() -> enableCircleProgressView(false));
    }


    @Subscribe
    public void progressSubscribe(ProgressBus progressBus) {
        runOnUiThread(() -> {
            long contentLength = progressBus.getContentLength();
            long bytesRead = progressBus.getBytesRead();
            if (contentLength == 0) {
                if (bytesRead == 0) contentLength = 1;
                else contentLength = bytesRead;
            }
            setProgressCount((int) (bytesRead * 100 / contentLength));
        });

    }

    @Click(R.id.activity_main_tv_organization)
    void tvOrganizationClick() {
        if (organizationUnitList != null) {
            dialog = AutocompleteDialog.newInstance(organizationUnitList, (model) -> {
                if (dialog != null) {
                    dialog.dismiss();
                }
                tvOrganization.setText(model.getDisplayName());
                organizationUnit = model;
                //update program list
                programList = model.getPrograms();
                program = null;
                tvProgram.setText("");

                updateBtSearch();
            });
            dialog.show(getSupportFragmentManager());
        }
    }

    @Click(R.id.activity_main_tv_program)
    void tvProgramClick() {
        if (programList != null) {
            dialog = AutocompleteDialog.newInstance(programList, (model) -> {
                if (dialog != null) {
                    dialog.dismiss();
                }
                tvProgram.setText(model.getDisplayName());
                program = model;
                updateBtSearch();
            });
            dialog.show(getSupportFragmentManager());
        }
    }

    @Click(R.id.activity_main_bt_enroll)
    void btEnrollClick() {
        if (program != null && organizationUnit != null) {
            navigator.applyCommand(new Forward(Screens.ENROLL_PROGRAM, null));
        } else {
            Toast.makeText(application, "Need select org and program first", Toast.LENGTH_SHORT).show();
        }
    }

    @Click(R.id.activity_main_bt_search)
    void btSearch() {
       // removed by ifhaam presenter.queryPrograms(organizationUnit.getId(), program.getId(), ProgramStatus.ACTIVE);
        presenter.downloadInstances(organizationUnit,program);

    }

    @Click(R.id.activity_main_bt_search_local)
    void btSerachLocal(){
        presenter.getInstancesLocal(program,organizationUnit);
    }

    private void updateBtSearch() {
        if (program != null && organizationUnit != null) {
            btSearch.setEnabled(true);
            btEnroll.setEnabled(true);
        } else {
            btSearch.setEnabled(false);
            btEnroll.setEnabled(false);
        }
    }

    private DrawerItem createItemFor(MenuItem menuItem) {
        return new SimpleItem(menuItem.getTitle(), ContextCompat.getDrawable(this, menuItem.getIcon()));
    }

    public Program getProgram(){
        //Observable<Program> a =  new DefaultProgramService().getProgramDetail("");
        return null;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
