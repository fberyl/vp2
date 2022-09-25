package listener

interface CardAnimalListener {
    fun OnEditClicked(position: Int)
    fun OnDeleteClicked(position: Int)
}