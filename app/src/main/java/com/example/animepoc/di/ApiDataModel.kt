data class Anime(
    val mal_id: Int,
    val title: String,
    val episodes: Int?,
    val rating: String?,
    val images: Images
)
data class Images(
    val jpg: ImageUrls
)
data class ImageUrls(
    val image_url: String,
    val large_image_url: String
)
data class AnimeResponse(
    val data: List<Anime>
)
// For Details screen
data class AnimeDetails(
    val data : Details
)
data class Details(
    val mal_id: Int,
    val url: String,
    val images: Images,
    val trailer: Trailer,
    val title: String,
    val episodes: Int,
    val rating: String,
    val score: Double,
    val rank: Int,
    val favorites: Int,
    val broadcast: Broadcast,
    val genres: List<Genre>,
)
data class Trailer(
    val youtube_id: String,
    val url: String,
    val embed_url: String,
    val images: TrailerImages
)
data class TrailerImages(
    val image_url: String,
    val small_image_url: String,
    val medium_image_url: String,
    val large_image_url: String,
    val maximum_image_url: String
)
data class Broadcast(
    val day: String?,
    val time: String?,
    val timezone: String?,
    val string: String?
)
data class Genre(
    val mal_id: Int,
    val type: String,
    val name: String,
    val url: String
)

