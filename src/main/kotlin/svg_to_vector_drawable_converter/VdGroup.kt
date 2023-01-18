package svg_to_vector_drawable_converter

internal class VdGroup : VdElement() {
    // Children can be either a {@link VdPath} or {@link VdGroup}
    val children = ArrayList<VdElement>()

    init {
        mName = this.toString() // to ensure paths have unique names
    }

    fun add(pathOrGroup: VdElement) {
        children.add(pathOrGroup)
    }

    fun size(): Int {
        return children.size
    }
}