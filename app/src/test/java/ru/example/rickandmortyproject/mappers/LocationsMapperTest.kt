package ru.example.rickandmortyproject.mappers

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import ru.example.rickandmortyproject.data.locations.mapper.LocationsMapper
import ru.example.rickandmortyproject.data.locations.model.LocationDbModel
import ru.example.rickandmortyproject.data.locations.model.LocationsDto

class LocationsMapperTest {
    class LocationsMapperTest {
        private lateinit var locationsMapper: LocationsMapper

        @Mock
        lateinit var mockLocationsDto: LocationsDto

        @Mock
        lateinit var mockLocationDbModel: LocationDbModel

        @Before
        fun setup() {
            MockitoAnnotations.openMocks(this)
            locationsMapper = LocationsMapper()
        }

        @Test
        fun `test mapping LocationsDto to LocationDbModel`() {
            `when`(mockLocationsDto.id).thenReturn(1)
            `when`(mockLocationsDto.name).thenReturn("Earth")

            val result = locationsMapper.mapDtoToDbModel(mockLocationsDto)

            assertEquals(1, result.id)
            assertEquals("Earth", result.name)
        }

        @Test
        fun `test mapping list of LocationDbModel to list of LocationEntity`() {
            val locationDbModelList = listOf(mockLocationDbModel)
            `when`(mockLocationDbModel.id).thenReturn(1)
            `when`(mockLocationDbModel.name).thenReturn("Earth")

            val result = locationsMapper.mapDbModelsListToEntitiesList(locationDbModelList)

            assertEquals(1, result.size)
            assertEquals(1, result[0].id)
            assertEquals("Earth", result[0].name)
        }
    }
}
