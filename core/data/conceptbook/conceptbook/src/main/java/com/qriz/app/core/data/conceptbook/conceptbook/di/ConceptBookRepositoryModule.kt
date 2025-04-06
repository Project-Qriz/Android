package com.qriz.app.core.data.conceptbook.conceptbook.di

import com.qriz.app.core.data.conceptbook.conceptbook.repository.ConceptBookRepositoryImpl
import com.qriz.app.core.data.conceptbook.conceptbook_api.repository.ConceptBookRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ConceptBookRepositoryModule {

    @Binds
    @Singleton
    fun bindsConceptBookRepository(
        conceptBookRepository: ConceptBookRepositoryImpl
    ): ConceptBookRepository
}
