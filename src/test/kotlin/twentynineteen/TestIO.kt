package twentynineteen

import IOInterface

class TestIO: IOInterface {
    val outputs = mutableListOf<Int>()
    override fun fetchInput(): Int {
        return -1 // TODO, enable sample inputs to get passed in
    }

    override fun postOutput(value: Int) {
        outputs.add(value)
    }
}
