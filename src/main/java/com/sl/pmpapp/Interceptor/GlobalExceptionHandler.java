package com.sl.pmpapp.Interceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sl.pmpapp.utils.CodeMsg;
 
@ControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
 
    /**
     * 系统异常处理，比如：404,500
     * @param req
     * @param resp
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public CodeMsg defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        logger.error("", e);
        if (e instanceof org.springframework.web.servlet.NoHandlerFoundException) {
        	return CodeMsg.MISSING_PATH;
        } else {
        	return CodeMsg.SERVER_ERROR;
        }
         
    }
}
