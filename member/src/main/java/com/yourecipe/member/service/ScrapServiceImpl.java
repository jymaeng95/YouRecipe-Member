package com.yourecipe.member.service;

import com.yourecipe.member.model.Scrap;
import com.yourecipe.member.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ScrapServiceImpl implements ScrapService{
    private final ScrapRepository scrapRepository;

    @Override
    public boolean doScrap(Scrap scrap) {
        return scrapRepository.addScrap(scrap) > 0;
    }

    @Override
    public boolean cancelScrap(int recipeId) {
        return scrapRepository.deleteScrap(recipeId) > 0;
    }

    @Override
    public boolean clearScrapList(int memberId) {
        return scrapRepository.deleteAllScrap(memberId) > 0;
    }

    @Override
    public Optional<List<Scrap>> getScrapList(int memberId) {

        return Optional.ofNullable(scrapRepository.selectScrapList(memberId));
    }
}
