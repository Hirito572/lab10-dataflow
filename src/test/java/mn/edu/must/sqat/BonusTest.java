
package mn.edu.must.sqat;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BonusTest {

    @Test
    void testAllDefsScoreFiveAndEight() {
        assertEquals(8, DataFlowTargets.bonus(
                new int[]{1, 1, 1, 1, 1}, 5));
    }

    @Test
    void testAllDefsScoreZeroAndStreakReset() {
        assertEquals(0, DataFlowTargets.bonus(
                new int[]{0, 1}, 3));
    }
}
