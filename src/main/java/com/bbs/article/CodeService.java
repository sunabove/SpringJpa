package com.bbs.article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CodeService extends ServicCommon {  

	private static final long serialVersionUID = -3136955053486112227L;
	
	@Autowired public CodeRepository codeRepository;

	public CodeService() {
	} 
	
	public Code getCode( String codeId , String def , Integer ord ) {
		Code code = codeRepository.findByCodeId( codeId ) ;
		
		if( null == code && codeId.contains( "-" ) ) {
			String grpCodeId =  codeId.substring( 0, codeId.lastIndexOf( "-" ) );
			
			Code grpCode = this.getCode( grpCodeId , grpCodeId , 0 ) ; 
			
			if( null != grpCode ) { 
				code = new Code();
				code.grpCode = grpCode ; 
				code.codeId = codeId ;
				code.textValue = def ; 
				code.ord = ord;
				
				code = codeRepository.save( code );
			}
		} else if( null == code ) {
			code = new Code();
			code.codeId = codeId ;
			code.textValue = def ;
			code.ord = ord;
			
			code = codeRepository.save( code );
		}
		
		return code;
	} 
	
	public Code saveCode( Code code ) {
		if( null != code ) {
			code = this.codeRepository.save( code );
		}
		
		return code ;
	}

}