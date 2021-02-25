package web.model;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface PropRepository extends PagingAndSortingRepository<Prop, String> {
	
	Prop findByPropId(String propId);
	
}