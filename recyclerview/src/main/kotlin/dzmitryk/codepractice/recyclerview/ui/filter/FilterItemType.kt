package dzmitryk.codepractice.recyclerview.ui.filter

enum class FilterItemType(
    val emoji: String
) {
    BANANA("🍌"),
    APPLE("🍎"),
    ORANGE("🍊"),
    GRAPES("🍇"),
    WATERMELON("🍉"),
    PINEAPPLE("🍍"),
    STRAWBERRY("🍓"),
    KIWI("🥝"),
    CHERRY("🍒"),
    PEACH("🍑"),
}

data class FilterItem(
    val text: String,
    val type: FilterItemType
)