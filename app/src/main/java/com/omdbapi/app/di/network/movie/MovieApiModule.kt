package com.omdbapi.app.di.network.movie

import com.omdbapi.app.network.movie.MovieApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
class MovieApiModule {

    @Provides
    @ViewModelScoped
    fun provideAuthApiInterface(retrofit: Retrofit): MovieApiInterface {
        return retrofit.create(MovieApiInterface::class.java)
    }
}