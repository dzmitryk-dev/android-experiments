package demo.memeviewer.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import demo.memeviewer.data.DataSource
import demo.memeviewer.data.NetworkSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindDataSource(impl: NetworkSource): DataSource

    companion object {

        @Provides
        @Singleton
        @IoDispatcher
        fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
    }
}