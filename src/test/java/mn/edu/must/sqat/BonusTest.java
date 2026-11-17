
package mn.edu.must.sqat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BonusTest {

    // ALL-DEFS tests
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

    // ALL-DU-PAIRS additional tests
    @Test
    void testInitialPresentDefToScoreTrue() {
        assertEquals(5, DataFlowTargets.bonus(
                new int[]{}, 0));
    }

    @Test
    void testInitialPresentDefToScoreFalse() {
        assertEquals(0, DataFlowTargets.bonus(
                new int[]{}, 1));
    }

    @Test
    void testScoreInitialDefToAddition() {
        assertEquals(3, DataFlowTargets.bonus(
                new int[]{1, 1, 1, 1, 1}, 6));
    }

    @Test
    void testStreakResetAndBestFalseBranch() {
        assertEquals(5, DataFlowTargets.bonus(
                new int[]{1, 0, 1}, 2));
    }

    @Test
    void testNullAttendance() {
        assertThrows(IllegalArgumentException.class,
                () -> DataFlowTargets.bonus(null, 0));
    }

    @Test
    void testNegativeThreshold() {
        assertThrows(IllegalArgumentException.class,
                () -> DataFlowTargets.bonus(new int[]{}, -1));
    }

    @Test
void testScoreFiveDirectlyReturned() {
    assertEquals(5, DataFlowTargets.bonus(
            new int[]{1, 0}, 1));
}
}
