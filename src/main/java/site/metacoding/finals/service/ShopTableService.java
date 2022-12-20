package site.metacoding.finals.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.metacoding.finals.config.auth.PrincipalUser;
import site.metacoding.finals.domain.shop.Shop;
import site.metacoding.finals.domain.shop.ShopRepository;
import site.metacoding.finals.domain.shop_table.ShopTable;
import site.metacoding.finals.domain.shop_table.ShopTableRepository;
import site.metacoding.finals.dto.repository.shop.QtyTableDto;
import site.metacoding.finals.dto.shop_table.ShopTableReqDto.ShopTableUpdateReqDto;
import site.metacoding.finals.dto.shop_table.ShopTableReqDto.ShopTableUpdateReqDto.ShopTableQtyDto;
import site.metacoding.finals.dto.shop_table.ShopTableRespDto.ShopTableListRespDto;
import site.metacoding.finals.dto.shop_table.ShopTableRespDto.ShopTableSaveRespDto;

@Slf4j
@RequiredArgsConstructor
@Service
public class ShopTableService {

    private final ShopTableRepository shopTableRepository;
    private final ShopRepository shopRepository;

    @Transactional
    public List<ShopTableListRespDto> findAllByShopId(PrincipalUser principalUser) {
        // Shop shopPS = shopRepository.findByUserId(userId)
        // .orElseThrow(() -> new RuntimeException("해당 가게가 없습니다."));
        List<QtyTableDto> dtos = shopTableRepository.findQtyTable();

        return dtos.stream().map((d) -> new ShopTableListRespDto(d)).collect(Collectors.toList());

        // return new
        // AllShopTableRespDto(shopTableRepository.findByShopId(shopPS.getId()));
    }

    @Transactional
    public void save(ShopTableUpdateReqDto shopTableUpdateReqDto) {
    }

    @Transactional
    public ShopTableSaveRespDto update(ShopTableUpdateReqDto shopTableUpdateReqDto, PrincipalUser principalUser) {

        for (ShopTableQtyDto shopTableDto : shopTableUpdateReqDto.getShopTableQtyDtoList()) {
            if (shopTableDto.getQty() > 0) {
                for (int i = 0; i < shopTableDto.getQty(); i++) {
                    shopTableRepository
                            .save(shopTableUpdateReqDto.toShopTableEntity(shopTableDto.getMaxPeople(),
                                    principalUser.getShop()));
                    log.debug("디버그 : shopTableUpdateReqDto 값" + shopTableUpdateReqDto
                            .toShopTableEntity(shopTableDto.getMaxPeople(), principalUser.getShop()).getMaxPeople());
                    log.debug("디버그 : save로직 실행 완료");
                }
            }
            if (shopTableDto.getQty() < 0) {
                for (int i = 0; i < Math.abs(shopTableDto.getQty()); i++) {
                    ShopTable shopTable = shopTableRepository.findByMaxPeopleToMinId(principalUser.getShop().getId(),
                            shopTableDto.getMaxPeople());
                    log.debug("아이디 " + i + "번째" + shopTable.getId());
                    shopTableRepository.delete(shopTable);
                }
            }
            log.debug("디버그 : delete로직 실행 완료");
        }

        return new ShopTableSaveRespDto(shopTableRepository.findByShopId(principalUser.getShop().getId()));
    }
}
