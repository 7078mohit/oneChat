package com.example.chattingappscreens.presentation.NavGraph

sealed class Route (val name : String){

    object Home : Route("HomeMain")
    object Out : Route("OutMain")
    object Auth : Route("AuthMain")
}

sealed class Home(val name : String){
    object Contact : Home("Contact")
    object Profile : Home("Profile")
    object EditProfile : Home("editProfile")
    object ChatsSetting : Home("chatsSetting")
    object Notification : Home("notification")
    object Storage : Home("storage")
    object Help : Home("help")
    object About : Home("about")
}

sealed class Out(val name : String){
    object Chatting : Out("Chatting")
    object Search : Out("Search")
    object Webview : Out("webView")            // for privacy show
    object Call : Out("Calling")
    object Ringing : Out("Ringing")
    object SendRinging : Out("SendRinging")
}

sealed class Auth (val name : String) {
    object Welcome : Auth("Welcome")
    object Signup : Auth("Signup")
    object SignIn : Auth("SignIn")
}





// bad me krte hai bro
sealed class Onboarding(val name : String){

}

sealed class SplashScreen(val name : String){

}