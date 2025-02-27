/*
 * Copyright (C) 2018, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.datmusic.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import tm.alashow.base.billing.SubscriptionsInitializer
import tm.alashow.base.imageloading.CoilAppInitializer
import tm.alashow.base.inititializer.AppInitializers
import tm.alashow.base.inititializer.ThreeTenAbpInitializer
import tm.alashow.base.inititializer.TimberInitializer
import tm.alashow.base.ui.utils.extensions.androidId
import tm.alashow.base.util.CoroutineDispatchers
import tm.alashow.base.util.LocalConfig
import tm.alashow.datmusic.fcm.FcmTokenRegistrator
import tm.alashow.datmusic.notifications.NotificationsInitializer
import tm.alashow.datmusic.util.RemoteConfigInitializer

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun coroutineDispatchers() = CoroutineDispatchers(
        network = Dispatchers.IO,
        io = Dispatchers.IO,
        computation = Dispatchers.Default,
        main = Dispatchers.Main
    )

    @Provides
    fun appContext(app: Application): Context = app.applicationContext

    @Provides
    fun appResources(app: Application): Resources = app.resources

    @Singleton
    @Provides
    fun provideFirebaseAnalytics(app: Application): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(app).apply {
            setUserId(app.androidId())
        }
    }

    @Provides
    @Singleton
    fun localConfig(app: Application) = LocalConfig(app.getSharedPreferences("local_config", Context.MODE_PRIVATE))

    @Provides
    fun appInitializers(
        notifications: NotificationsInitializer,
        timberManager: TimberInitializer,
        threeTen: ThreeTenAbpInitializer,
        coilAppInitializer: CoilAppInitializer,
        fcmTokenRegistrator: FcmTokenRegistrator,
        remoteConfigInitializer: RemoteConfigInitializer,
        subscriptionsInitializer: SubscriptionsInitializer,
    ): AppInitializers {
        return AppInitializers(
            notifications,
            timberManager,
            threeTen,
            coilAppInitializer,
            fcmTokenRegistrator,
            remoteConfigInitializer,
            subscriptionsInitializer
        )
    }
}
