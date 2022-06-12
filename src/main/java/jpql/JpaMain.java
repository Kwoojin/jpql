package jpql;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            Team team = new Team();
            team.setName("A");
            em.persist(team);

            Member member = new Member();
            member.setUsername("memberA");
            member.setAge(25);
            member.setTeam(team);

            em.persist(member);

            Member memberB = new Member();
            memberB.setUsername("memberB");
            memberB.setAge(23);
            memberB.setTeam(team);

            em.persist(memberB);

            Product product = new Product();
            product.setName("Sugar");
            product.setPrice(1000);
            product.setStockAmount(10);

            em.persist(product);

            Address address = new Address();
            address.setCity("A");
            address.setStreet("B");
            address.setZipcode("C");

            Order order = new Order();
            order.setProduct(product);
            order.setAddress(address);
            order.setOrderAmount(5);

            em.persist(order);

            em.flush();
            em.clear();

            /**
             * TypedQuery - 반환 타입이 명확할 때
             * Query - 반환 타입이 명확하지 않을 때
             * 
             * [Parameter]
             * :name -> setParameter("name", "value");
             * ?1 -> setParameter(1, "value"); // 위치 기반은 쓰지 말자
             */
//            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
//            TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);

//            Query query3 = em.createQuery("select m.username, m.age from Member m");

//            Member query4SingleResult = em.createQuery("select m from Member m where m.username = :username", Member.class)
//                    .setParameter("username", "memberA")
//                    .getSingleResult();
//            System.out.println("query4SingleResult = " +

//            List list = em.createQuery("select o.id, o.address.city from Order o").getResultList();

            /*
            Object o = list.get(0);
            Object[] result = (Object[]) o;
            System.out.println("result[0] = " + result[0]);
            System.out.println("result[0] = " + result[1]);

            List<Object []> list = em.createQuery("select o.id, o.address.city from Order o").getResultList();
            Object[] result = list.get(0);
            System.out.println("result[0] = " + result[0]);
            System.out.println("result[0] = " + result[1]);
            */


//            list.stream().forEach(od -> {
//                System.out.println("od = " + od.g);
//                System.out.println("id = " + od.getId() + " city = " + order.getAddress().getCity());
//                +" street = " + order.getAddress().getStreet() + " quantity" + order.getOrderAmount());
//            });


            List<MemberDTO> resultList = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class).getResultList();

            resultList.stream().forEach(rs -> {
                System.out.println("rs = " + rs);
            });

            /**
             * getResultList
             * 결과가 없으면 빈 리스트 반환
             * getSingleResult
             * 결과는 무조건 하나만!
             * - 결과가 없으면 javax.persistence.NoResultException
             * - 둘 이상이면 javax.persistence.NonUniqueResultException
             * - Spring Data JPA -> 결과가 없으면 Null Or Optional
             */
            /*
            List<Member> members = em.createQuery("select m from Member m", Member.class)
                    .getResultList();

            members
                    .stream()
                    .forEach(m -> {
                        System.out.println("m = " + m);
                    });

            Member findMember = em.createQuery("select m from Member m WHERE m.id = 1", Member.class)
                    .getSingleResult();
             */

            tx.commit();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            tx.rollback();
        } finally {
            em.clear();
        }
        emf.close();
    }
}

