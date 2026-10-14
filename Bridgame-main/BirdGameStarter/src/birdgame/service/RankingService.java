/*
 * จัดอันดับผู้เล่นตามกติกาของเกม
 * ผู้ที่ถึงรังมาก่อนจะอยู่ก่อน จากนั้นจึงดูด่านสูงสุดและจำนวนท่อสะสม
 */

package birdgame.service;

import birdgame.model.Player;

import birdgame.model.RankingEntry;

import java.util.ArrayList;

import java.util.Comparator;

import java.util.List;

public class RankingService {

    private int finishCounter = 0;

    // ให้เลข finishOrder ตามลำดับที่ผู้เล่นถึงรังจริง
    public void recordWinner(Player player) {

        if (!player.isFinished()) {

            finishCounter++;

            player.finishGame(finishCounter);
        }
    }

    // จัดอันดับตามกติกา: ถึงรังก่อน > ด่านสูงกว่า > ท่อมากกว่า > playerId
    public List<RankingEntry> calculateRanking(List<Player> players) {

        List<Player> copy = new ArrayList<>(players);

        copy.sort(

            Comparator
                .comparing(Player::isFinished).reversed()
                .thenComparingInt(p -> p.isFinished() ? p.getFinishOrder() : 0)
                .thenComparing(

                    Comparator.comparingInt(Player::getCurrentLevel).reversed()
                )
                .thenComparing(

                    Comparator.comparingInt(Player::getTotalPipes).reversed()
                )
                .thenComparingInt(Player::getPlayerId)
        );

        return copy.stream()
                .map(RankingEntry::new)

                .toList();
    }
}
