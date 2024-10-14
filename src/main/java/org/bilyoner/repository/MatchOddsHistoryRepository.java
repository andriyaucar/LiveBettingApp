package org.bilyoner.repository;

import org.bilyoner.model.MatchOddsHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchOddsHistoryRepository extends JpaRepository<MatchOddsHistory, Long> {
    List<MatchOddsHistory> findAllByOrderByCreatedAtAsc();
}
