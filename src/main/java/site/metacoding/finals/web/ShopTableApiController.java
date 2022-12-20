package site.metacoding.finals.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.metacoding.finals.config.annotation.VerifyShop;
import site.metacoding.finals.config.auth.PrincipalUser;
import site.metacoding.finals.dto.ResponseDto;
import site.metacoding.finals.dto.shop_table.ShopTableReqDto.ShopTableUpdateReqDto;
import site.metacoding.finals.dto.shop_table.ShopTableRespDto.ShopTableListRespDto;
import site.metacoding.finals.dto.shop_table.ShopTableRespDto.ShopTableSaveRespDto;
import site.metacoding.finals.service.ShopTableService;

@RequiredArgsConstructor
@RestController
public class ShopTableApiController {

        private final ShopTableService shopTableService;

        // 테이블 생성, 삭제 (수정기능)
        @VerifyShop
        @PostMapping("/shop/table")
        public ResponseEntity<?> tableUpdate(@AuthenticationPrincipal PrincipalUser principalUser,
                        @RequestBody ShopTableUpdateReqDto shopTableUpdateReqDto) {
                System.out.println(principalUser.getShop().getId());
                System.out.println(shopTableUpdateReqDto.getShopTableQtyDtoList().get(0).getQty());
                ShopTableSaveRespDto shopTableSaveRespDto = shopTableService.update(shopTableUpdateReqDto,
                                principalUser);
                return new ResponseEntity<>(new ResponseDto<>(HttpStatus.OK, "테이블 수 변경 완료", shopTableSaveRespDto),
                                HttpStatus.OK);
        }

        @GetMapping("/shop/table")
        public ResponseEntity<?> tableList(@AuthenticationPrincipal PrincipalUser principalUser) {
                List<ShopTableListRespDto> allShopTableRespDto = shopTableService.findAllByShopId(principalUser);
                return new ResponseEntity<>(new ResponseDto<>(HttpStatus.OK, "가게 테이블 전체보기 성공", allShopTableRespDto),
                                HttpStatus.OK);
        }
}
