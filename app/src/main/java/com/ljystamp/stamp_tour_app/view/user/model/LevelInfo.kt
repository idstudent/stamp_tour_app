package com.ljystamp.stamp_tour_app.view.user.model

data class LevelInfo(
    val level: String,
    val currentCount: Int,
    val targetCount: Int,
    val progress: Int
)

enum class CategoryLevel(
    val subTitleFormat: String,
    val beginnerLevel: String,
    val intermediateLevel: String,
    val advancedLevel: String
) {
    TOUR(
        "여행지 %d곳을 완료 해보세요!",
        "여행 병아리",
        "방랑가",
        "콜럼버스"
    ),
    CULTURE(
        "문화시설 %d곳을 체험 해보세요!",
        "관람객",
        "예술람",
        "Art 그 자체"
    ),
    EVENT(
        "축제 %d곳을 즐겨보세요!",
        "가볍게 즐기는 사람",
        "축제에 미쳐버린 사람",
        "내가 바로 MC"
    ),
    ACTIVITY(
        "액티비티 %d개를 즐겨보세요!",
        "뉴비",
        "고인물",
        "통달한 사람"
    ),
    FOOD(
        "식당 %d곳을 방문해보세요!",
        "맛집 찾아 헤매는 뉴비",
        "고독한 미식가",
        "먹방 신"
    )
}

