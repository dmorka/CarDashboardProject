package org.Logic;

import java.util.Comparator;

public class journeyDistanceComperator implements Comparator<OnBoardComputer> {
    public int compare(OnBoardComputer onBoardComputer1, OnBoardComputer onBoardComputer2) {
        return Float.compare(onBoardComputer1.getJourneyDistance(), onBoardComputer2.getJourneyDistance());
    }
}
