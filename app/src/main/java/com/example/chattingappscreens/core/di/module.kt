package com.example.chattingappscreens.core.di

import com.example.chattingappscreens.core.common.NetworkObserver
import com.example.chattingappscreens.core.common.OnlineStatusManager
import com.example.chattingappscreens.core.preference.PreferenceData
import com.example.chattingappscreens.core.preference.dataStore
import com.example.chattingappscreens.data.repository.AuthRepoImpl
import com.example.chattingappscreens.data.repository.ChatRepoImpl
import com.example.chattingappscreens.data.repository.HomeRepoImpl
import com.example.chattingappscreens.domain.repository.AuthRepository
import com.example.chattingappscreens.domain.repository.ChatRepository
import com.example.chattingappscreens.domain.repository.HomeRepository
import com.example.chattingappscreens.domain.usecase.Auth.DeleteUserUseCase
import com.example.chattingappscreens.domain.usecase.Auth.LogOutUseCase
import com.example.chattingappscreens.domain.usecase.Auth.SignInWithEmailUseCase
import com.example.chattingappscreens.domain.usecase.Auth.SignUpWithEmailUseCase
import com.example.chattingappscreens.domain.usecase.Auth.SigningWithGoogleUseCase
import com.example.chattingappscreens.domain.usecase.Auth.UpdateOnlineStatusUseCase
import com.example.chattingappscreens.domain.usecase.Auth.UpdateUserProfileUseCase
import com.example.chattingappscreens.domain.usecase.Auth.UpdateUserUseCase
import com.example.chattingappscreens.domain.usecase.Chat.AddChatUseCase
import com.example.chattingappscreens.domain.usecase.Chat.DeleteChatUseCase
import com.example.chattingappscreens.domain.usecase.Chat.DeleteMessageUseCase
import com.example.chattingappscreens.domain.usecase.Chat.EditMessageUseCase
import com.example.chattingappscreens.domain.usecase.Chat.GetChatUseCase
import com.example.chattingappscreens.domain.usecase.Chat.GetFriendByUidUseCase
import com.example.chattingappscreens.domain.usecase.Chat.GetMessageUseCase
import com.example.chattingappscreens.domain.usecase.Chat.IsPopUpShowed
import com.example.chattingappscreens.domain.usecase.Chat.OfflineMarkUnreadCountAndIsReadUseCase
import com.example.chattingappscreens.domain.usecase.Chat.OnlineMarkUnreadCountAndIsReadUseCase
import com.example.chattingappscreens.domain.usecase.Chat.PopUpRead
import com.example.chattingappscreens.domain.usecase.Chat.SaveTokenUseCase
import com.example.chattingappscreens.domain.usecase.Chat.SendMessageUseCase
import com.example.chattingappscreens.domain.usecase.Chat.StopIsReadLiveUseCase
import com.example.chattingappscreens.domain.usecase.Home.GetAllUsersUserCase
import com.example.chattingappscreens.domain.usecase.Home.GetUserByIdUseCase
import com.example.chattingappscreens.domain.usecase.Home.SearchUserUseCase
import com.example.chattingappscreens.viewmodel.AuthViewModel
import com.example.chattingappscreens.viewmodel.ChatViewModel
import com.example.chattingappscreens.viewmodel.HomeViewModel
import com.example.chattingappscreens.viewmodel.SharedViewModel
import com.example.chattingappscreens.viewmodel.ThemeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module



val RepoModules = module {
        singleOf(::AuthRepoImpl){bind<AuthRepository>()}
        singleOf(::HomeRepoImpl){bind<HomeRepository>()}
        singleOf(::ChatRepoImpl){bind<ChatRepository>()}
       // singleOf(::CallRepoImpl){bind<CallRepository>()}
    }

    val AuthUseCaseModules = module {
        factory { SignInWithEmailUseCase(get()) }
        factory { SignUpWithEmailUseCase(get()) }
        factory { SigningWithGoogleUseCase(get()) }
        factory{ LogOutUseCase(get()) }
        factory { UpdateUserProfileUseCase(get()) }
        factory { UpdateOnlineStatusUseCase(get()) }
        factory { DeleteUserUseCase(get())}
        factory { UpdateUserUseCase(get()) }
    }

    val HomeUseCaseModules = module {
        factory { GetUserByIdUseCase(get())}
        factory { SearchUserUseCase(get())}
        factory { GetAllUsersUserCase(get())}
    }

    val ChatUseCaseModules = module {
        factory { AddChatUseCase(get()) }
        factory { GetChatUseCase(get()) }
        factory { GetFriendByUidUseCase(get()) }
        factory { GetMessageUseCase(get()) }
        factory { SendMessageUseCase(get()) }
        factory { OnlineMarkUnreadCountAndIsReadUseCase(get()) }
        factory { OfflineMarkUnreadCountAndIsReadUseCase(get()) }
        factory { StopIsReadLiveUseCase(get()) }
        factory { DeleteChatUseCase(get()) }
        factory { EditMessageUseCase(get()) }
        factory { DeleteMessageUseCase(get()) }
        factory { SaveTokenUseCase(get()) }
        factory { IsPopUpShowed(get()) }
        factory { PopUpRead(get()) }
    }

    val viewModelModules = module {
        viewModelOf(::AuthViewModel)
        viewModelOf(::HomeViewModel)
        viewModelOf(::ChatViewModel)
        viewModelOf(::SharedViewModel)
        viewModelOf(::ThemeViewModel)
    }

    val firebaseModules = module {
        single { FirebaseAuth.getInstance() }
        single { FirebaseDatabase.getInstance() }
        single { FirebaseStorage.getInstance() }
        single { FirebaseMessaging.getInstance() }
    }

    val preferenceModule = module {
        single{androidContext().dataStore}
        single { PreferenceData(get()) }
    }

    val onlineStatusManagerModule = module{
        factory { (uid:String) -> OnlineStatusManager(get(),uid)  }
    }

    val networkObserverModule = module {
        single { NetworkObserver(get()) }
    }

