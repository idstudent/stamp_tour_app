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
        "여행 초보자",
        "여행 매니아",
        "여행 달인"
    ),
    CULTURE(
        "문화시설 %d곳을 체험 해보세요!",
        "문화 초보자",
        "문화 애호가",
        "문화 전문가"
    ),
    EVENT(
        "축제 %d곳을 즐겨보세요!",
        "축제 입문자",
        "축제 마니아",
        "축제 달인"
    ),
    ACTIVITY(
        "액티비티 %d개를 즐겨보세요!",
        "도전 초보자",
        "도전 열정가",
        "도전 마스터"
    ),
    FOOD(
        "식당 %d곳을 방문해보세요!",
        "맛집 초보자",
        "맛집 탐험가",
        "맛집 미식가"
    )
}

