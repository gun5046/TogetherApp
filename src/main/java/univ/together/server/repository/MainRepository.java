package univ.together.server.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;


import lombok.RequiredArgsConstructor;
import univ.together.server.model.Member;

@Repository
@RequiredArgsConstructor
public class MainRepository {

	private final EntityManager em;
	
	public List<Member> findProjectByUserIdx(Long user_idx) {
		
		return em.createQuery("SELECT m FROM Member m " + 
						"JOIN FETCH m.project_idx " + 
						"WHERE m.user_idx.user_idx = :user_idx", Member.class)
			.setParameter("user_idx", user_idx)
			.getResultList();

	}
	
}
