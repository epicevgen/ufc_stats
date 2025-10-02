package com.ufcstats.service;

import com.ufcstats.dto.RatingChangeDto;
import com.ufcstats.model.Fight;
import com.ufcstats.model.enums.FightMode;
import com.ufcstats.repository.FightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Сервис для расчета изменений рейтинга и места в рейтинге
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RatingChangeService {
    
    private final FightRepository fightRepository;
    
    /**
     * Рассчитывает изменения рейтинга и места в рейтинге для списка боев
     * @param fights список боев
     * @return Map с ключом - ID боя, значением - данные об изменениях рейтинга
     */
    public Map<Long, RatingChangeDto> calculateRatingChanges(List<Fight> fights) {
        log.debug("Расчет изменений рейтинга для {} боев", fights.size());
        
        Map<Long, RatingChangeDto> ratingChanges = new HashMap<>();
        
        for (Fight fight : fights) {
            RatingChangeDto ratingChange = calculateRatingChangeForFight(fight);
            ratingChanges.put(fight.getId(), ratingChange);
        }
        
        return ratingChanges;
    }
    
    /**
     * Рассчитывает изменения рейтинга и места в рейтинге для конкретного боя
     * @param fight бой
     * @return данные об изменениях рейтинга
     */
    private RatingChangeDto calculateRatingChangeForFight(Fight fight) {
        log.debug("Расчет изменений рейтинга для боя ID: {}, режим: {}", fight.getId(), fight.getFightMode());
        
        // Получаем предыдущий бой того же режима, отсортированный по дате
        Fight previousFight = getPreviousFightByMode(fight.getFightMode(), fight.getFightDate());
        
        Integer currentRatingPoints = fight.getRatingPoints();
        Integer currentRankingPosition = fight.getRankingPosition();
        
        Integer previousRatingPoints = null;
        Integer previousRankingPosition = null;
        
        if (previousFight != null) {
            previousRatingPoints = previousFight.getRatingPoints();
            previousRankingPosition = previousFight.getRankingPosition();
        }
        
        // Рассчитываем изменения
        Integer ratingPointsChange = null;
        Integer rankingPositionChange = null;
        
        if (currentRatingPoints != null && previousRatingPoints != null) {
            ratingPointsChange = currentRatingPoints - previousRatingPoints;
        }
        
        if (currentRankingPosition != null && previousRankingPosition != null) {
            // Для места в рейтинге: положительное изменение означает улучшение (место уменьшилось)
            rankingPositionChange = previousRankingPosition - currentRankingPosition;
        }
        
        return RatingChangeDto.builder()
                .currentRatingPoints(currentRatingPoints)
                .previousRatingPoints(previousRatingPoints)
                .ratingPointsChange(ratingPointsChange)
                .currentRankingPosition(currentRankingPosition)
                .previousRankingPosition(previousRankingPosition)
                .rankingPositionChange(rankingPositionChange)
                .fightMode(fight.getFightMode().name())
                .build();
    }
    
    /**
     * Получает предыдущий бой того же режима, отсортированный по дате
     * @param fightMode режим боя
     * @param currentDate текущая дата боя
     * @return предыдущий бой или null, если не найден
     */
    private Fight getPreviousFightByMode(FightMode fightMode, java.time.LocalDateTime currentDate) {
        log.debug("Поиск предыдущего боя для режима: {}, дата: {}", fightMode, currentDate);
        
        List<Fight> previousFights = fightRepository.findByFightModeAndFightDateBeforeOrderByFightDateDesc(
                fightMode, currentDate);
        
        if (previousFights.isEmpty()) {
            log.debug("Предыдущий бой не найден для режима: {}", fightMode);
            return null;
        }
        
        Fight previousFight = previousFights.get(0);
        log.debug("Найден предыдущий бой ID: {}, дата: {}, рейтинг: {}, место: {}", 
                previousFight.getId(), 
                previousFight.getFightDate(),
                previousFight.getRatingPoints(),
                previousFight.getRankingPosition());
        
        return previousFight;
    }
}
