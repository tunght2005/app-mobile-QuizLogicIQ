package com.example.logiciq.navigation

object Routes {
    // Root graphs
    const val AUTH = "auth"
    const val MAIN = "main"

    // Auth screens
    const val WELCOME = "welcome_screen"
    const val START = "start_screen"
    const val LOGIN = "login_screen"
    const val REGISTER = "register_screen"
    const val RESET = "reset_password_screen"

    // Main screens (sau khi đăng nhập)
    const val HOME = "home_screen"
    const val LIBRARY = "library_screen"
    const val CLASS = "class_screen"
    const val NEWCLASS = "create_class"
    const val NEWTEST = "create_test"
//    const val NEWSUBJECT = "create_subject"
//    const val SUBJECT = "suject_screen"
    const val TEST = "test_screen"
    const val EXAM = "exam_screen"
    const val LEARNING = "learning_screen"

    // Profile / Setting
    const val PROFILE = "profile_screen"
    const val SETTING = "setting_screen"
    const val HISTORY = "history_screen"
    const val CHANGE_PASSWORD = "change_password_screen"

    // Error / Others
    const val NOT_FOUND = "not_found_screen"
}
