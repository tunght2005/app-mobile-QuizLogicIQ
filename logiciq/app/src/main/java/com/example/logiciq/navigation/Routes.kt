package com.example.logiciq.navigation

object Routes {
    // Auth screens
    const val WELCOME = "welcome_screen"
    const val START = "start_screen"
    const val LOGIN = "login_screen"
    const val REGISTER = "register_screen"
    const val RESET = "reset_password_screen"
//    const val CONFIRM = "confirm_screen"
//    const val VERIFY_CODE = "verify_code_screen"

    // Main screens (sau khi đăng nhập)
    const val HOME = "home_screen"
    const val LIBRARY = "library_screen"
    const val CLASS = "class_screen"
    const val newCLASS = "create_class"
    const val newSUBJECT = "create_subject"
    const val QUIZ = "quiz_screen"
    const val QUIZ_DETAIL = "quiz_detail_screen"

    // Profile / Setting
    const val PROFILE = "profile_screen"
    const val SETTING = "setting_screen"
    const val CHANGE_PASSWORD = "change_password_screen"

    // Error / Others
    const val NOT_FOUND = "not_found_screen"
}
