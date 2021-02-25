package bbs.article;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping; 

@RequestMapping("/file")
@Controller
public class FileController extends ComController {

	private static final long serialVersionUID = -5704084995590809168L;

	@RequestMapping(value = { "index.html", "main.html", "list.html" })
	public String articleList(HttpServletRequest request) {

		return "530_article_list.html";
	}

	@RequestMapping( value = "sys/{file_id}", produces = MediaType.IMAGE_PNG_VALUE )
	public ResponseEntity<byte[]> getImage( @PathVariable("file_id") String file_id ) {

		byte[] contents = null ;
		
		if( null == contents ) {
			contents = new byte[ 0 ];
		} 
		
		if( this.isValid( file_id ) ) {
			var dbFile = this.dbFileRepository.findByFileId(file_id);
			if( null != dbFile ) {
				contents = dbFile.content ; 
			}
		}

		var bb = ResponseEntity.ok();
		bb.contentLength( null == contents ? 0 : contents.length );
		bb.contentType(MediaType.parseMediaType( MediaType.IMAGE_PNG_VALUE ));
		
		var re = bb.body(contents);
		
		return re;
	}

}