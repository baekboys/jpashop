package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Fail.fail;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class) // JUnit 실행 시 스프링과 함께 실행함
@SpringBootTest // 스프링컨테이너 안에서 테스트 해야 하는 경우, 없으면 Autowired 다 에러 발생
@Transactional // test에서는 기본적으로 rollback
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    //@Rollback(false) // insert가 되지만 rollback이 되면 영속성 컨텍스트에서 insert를 할 필요가 없으므로 로그에 insert구문이 나오지 않음, flush를 통해 확인 가능
    public void 회원가입() throws Exception {
        // given
        Member member = new Member();
        member.setName("baek");

        // when
        Long savedId = memberService.join(member);

        // then
        entityManager.flush(); // flush를 해야 insert 구문이 로그에서 확인 됨
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        // given
        Member member1 = new Member();
        member1.setName("baek");

        Member member2 = new Member();
        member2.setName("baek");

        // when
        memberService.join(member1);
        memberService.join(member2);

        // then
        fail("예외가 발생해야 한다.");
    }



}