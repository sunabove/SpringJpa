package web.model;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional

public class CodeService extends ServicCommon {  

	private static final long serialVersionUID = -3136955053486112227L;

	public CodeService() {

	} 
	
	public Code saveCode( Code code ) {
		if( null != code ) {
			code = this.codeRepository.save( code );
		}
		
		return code ;
	}

}