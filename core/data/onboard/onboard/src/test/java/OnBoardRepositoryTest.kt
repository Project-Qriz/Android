import com.qriz.app.core.data.onboard.onboard.repository.OnBoardRepositoryImpl
import com.qriz.app.core.network.onboard.api.OnBoardApi
import io.mockk.mockk

//TODO : 서버 API 수정 후 작성 예정
class OnBoardRepositoryTest {

    private val mockOnBoardApi = mockk<OnBoardApi>()

    private val bookSearchRepository = OnBoardRepositoryImpl(
        onBoardApi = mockOnBoardApi
    )
}
