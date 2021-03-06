package org.hisp.india.trackercapture.navigator;

import org.hisp.india.trackercapture.domains.enroll_program.EnrollProgramActivity;
import org.hisp.india.trackercapture.domains.enroll_program_stage.EnrollProgramStageActivity;
import org.hisp.india.trackercapture.domains.enroll_program_stage_detail.EnrollProgramStageDetailActivity;
import org.hisp.india.trackercapture.domains.login.LoginActivity;
import org.hisp.india.trackercapture.domains.main.MainActivity;
import org.hisp.india.trackercapture.domains.tracked_entity.TrackedEntityActivity;

/**
 * Created by nhancao on 4/20/17.
 */

public class Screens {
    public static final String MAIN_SCREEN = MainActivity.class.getSimpleName();
    public static final String LOGIN_SCREEN = LoginActivity.class.getSimpleName();
    public static final String ENROLL_PROGRAM = EnrollProgramActivity.class.getSimpleName();
    public static final String ENROLL_PROGRAM_STAGE = EnrollProgramStageActivity.class.getSimpleName();
    public static final String ENROLL_PROGRAM_STAGE_DETAIL = EnrollProgramStageDetailActivity.class.getSimpleName();
    public static final String TRACKED_ENTITY = TrackedEntityActivity.class.getSimpleName();

}
