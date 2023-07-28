package com.ehuman.oidc.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.ehuman.oidc.dto.EmpleadoDto;
import com.ehuman.oidc.dto.EmpleadoTokenDto;
import com.ehuman.oidc.dto.HuCatPropiedadesDto;


@Service
public class ConsultaDB {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private EmpleadoTokenService emplTokSer;

	
	private static final Logger LOG = LoggerFactory.getLogger(ConsultaDB.class);
	
	public String getUrlToken(String email) {
	//public String getUrlToken(Long numEmp, Long numComp) {
		//EmpleadoDto empEnc = recuperarRegistro(numEmp,numComp);//localiza datos de empleado en empleados intenos y esternos
		LOG.info("Ingresa a getUrlToken:" + email );
		EmpleadoDto empEnc = recuperarRegistro(email);
		EmpleadoTokenDto empTkEnc =  new EmpleadoTokenDto();
		String url = getUrl();
		if(empEnc.getApell_mat()!= null && empEnc.getApell_pat()!=null && 
				empEnc.getNombre()!= null &&empEnc.getNum_cia() != null && 
				empEnc.getNum_emp()!= null) {

			String username = empEnc.getApell_pat()+" "+empEnc.getApell_mat()+" "+empEnc.getNombre();


			//buscar en tabla de tokens si existe registro
			empTkEnc= emplTokSer.getEmpleadoToken(empEnc.getNum_cia(), empEnc.getNum_emp());
			LOG.info("Empleado encontrado: " +empTkEnc);



			if(empTkEnc.getNumCia()!= null && empTkEnc.getNumEmp()!= null && empTkEnc.getClass()!= null &&empTkEnc.getToken()!= null) {

				LOG.info("aQUI " +empTkEnc.toString());
				 emplTokSer.updateEmpleadoToken(empTkEnc, username);
				//return empTkEnc.toString();



			}else {
				LOG.info("aQUI else " +empEnc.toString());

				empTkEnc = emplTokSer.addRegistroEmpleado(empEnc);
				//return empTkEnc.toString();

			}
			
		
		}
		return url+"saml/sso_token.xhtml?tokenUrl="+empTkEnc.getToken();
//		else {
//			return url+"saml/sso_token.xhtml?tokenUrl=sin datos";
//		}
	}
	
	
	//obtiene registros de empleados internos o externos
			//public EmpleadoDto recuperarRegistro(Long numEmpleado, Long numeroCompania ) {
		   public EmpleadoDto recuperarRegistro(String email ) {
				LOG.info("Ingresa a recuperarRegistro");
				//String responseUrlRedirectWorkSocial = "";
				EmpleadoDto empleadoEncontrado =  null;	
				
				//EmpleadoDto empleadoDto= getEmpleado(numEmpleado, numeroCompania);
				EmpleadoDto empleadoDto= getEmpleado(email);
			if(empleadoDto.getApell_mat()!= null && empleadoDto.getApell_pat()!= null && empleadoDto.getNombre()!= null &&
						empleadoDto.getNum_cia()!= null && empleadoDto.getNum_emp()!= null && empleadoDto.getNumeroCompania()!= null) {
				//if(empleadoDto != null) {
					//responseUrlRedirectWorkSocial = "Empleado  interno encontrado " + empleadoDto.get(0).getNum_cia()+" " + empleadoDto.get(0).getNum_emp()+" "+ empleadoDto.get(0).getApell_pat();
					empleadoEncontrado = empleadoDto;
				}else {

					//if(empleadoDto.isEmpty()) {

					//EmpleadoDto empleadoExterno =getEmpletadoExterno(numEmpleado, numeroCompania);
					EmpleadoDto empleadoExterno =getEmpletadoExterno(email);
					if(empleadoExterno.getApell_mat()!= null && empleadoExterno.getApell_pat()!= null && empleadoExterno.getNombre()!= null &&
					empleadoExterno.getNum_cia()!= null && empleadoExterno.getNum_emp()!= null && empleadoExterno.getNumeroCompania()!= null) {
					//if(empleadoExterno!= null)	{
						empleadoEncontrado = empleadoExterno;
					}else if(empleadoDto.getNum_cia()== null || empleadoDto.getNum_emp()== null || 
							empleadoExterno.getNum_cia()!= null || empleadoExterno.getNum_emp()!= null) {
						empleadoEncontrado =  new EmpleadoDto();
					}
					

				}
				LOG.info("empleadoEncontrado: "+empleadoEncontrado);
				return empleadoEncontrado;
				

			}
	
	
			
	//consulta por numero de empleado y clave de compania internos
	
//	  public EmpleadoDto getEmpleado (Long numEmpleado, Long numeroCompania) {
//	  String sqlNumEmplClaveComp =
//	  "SELECT he.NUM_CIA, he.NUM_EMP, he.APELL_PAT,he.APELL_MAT,he.NOMBRE, hc.NUMERO_COMPANIA, hc.CLAVE_COMPANIA \r\n"
//	  +
//	  "FROM HUMAN.HU_EMPLS he, HUMAN.HU_COMPANIA hc WHERE he.NUM_CIA = hc.NUMERO_COMPANIA\r\n"
//	  + "AND he.STATUS = 'A' AND NUMERO_COMPANIA < 5000 AND he.NUM_EMP ="
//	  +" '"+numEmpleado+"' "+ "AND hc.NUMERO_COMPANIA="+" '"+numeroCompania +"' ";
//	  //List<EmpleadoDto>empleadoDtoNuevo = new ArrayList<>(); 
//	  EmpleadoDto emp =null;
//	  List<EmpleadoDto> empleadoDtoNuevo =jdbcTemplate.query(sqlNumEmplClaveComp,BeanPropertyRowMapper.newInstance(EmpleadoDto.class)); 
//	  LOG.info("En ConsultaDB: getEmpleado  obtiene"+ empleadoDtoNuevo.toString()); 
//	  if(!empleadoDtoNuevo.isEmpty()) { 
//		  emp =empleadoDtoNuevo.get(0);
//	  
//	  }else {
//		  emp = new EmpleadoDto();
//	  
//	  } return emp;//trae datos completos(nombre y apellidos) 
//	  }
	 
	
	//consulta en empleados externos
	
//	  public EmpleadoDto getEmpletadoExterno(Long numEmpleado, Long
//	  numeroCompania){
//	  
//	  String numEmplComp = this.completaNumEmp(numEmpleado);
//	  
//	  String sqlEmpleadosExternos
//	  ="SELECT substr(EXT.USER_SKEY,1,(length(EXT.USER_SKEY)-10)) AS NUM_CIA,\r\n"
//	  +
//	  "substr(EXT.USER_SKEY, length(substr(EXT.USER_SKEY,1,(length(EXT.USER_SKEY)-10)))+1 , (length(EXT.USER_SKEY))) AS NUM_EMP,\r\n"
//	  +
//	  "substr(EXT.USER_SKEY,1,(length(EXT.USER_SKEY)-10)) AS NUMERO_COMPANIA, \r\n"
//	  +
//	  "(SELECT CLAVE_COMPANIA FROM HUMAN.HU_COMPANIA WHERE NUMERO_COMPANIA = substr(EXT.USER_SKEY,1,(length(EXT.USER_SKEY)-10))) AS CLAVE_COMPANIA, \r\n"
//	  + "APELL_PAT, APELL_MAT, NOMBRE " + "FROM HUMAN.HU_USUARIO_EXTERNO EXT \r\n"
//	  + "WHERE  STATUS = 'A' \r\n" +
//	  "AND substr(EXT.USER_SKEY,1,(length(EXT.USER_SKEY)-10)) ="+
//	  " '"+numeroCompania+"'\r\n" +
//	  "AND substr(EXT.USER_SKEY, length(substr(EXT.USER_SKEY,1,(length(EXT.USER_SKEY)-10)))+1 , (length(EXT.USER_SKEY))) ="
//	  + " '" +numEmplComp+ "' "; EmpleadoDto empExterno = null; List<EmpleadoDto>
//	  empleadoExterno =
//	  jdbcTemplate.query(sqlEmpleadosExternos,BeanPropertyRowMapper.newInstance(
//	  EmpleadoDto.class)); LOG.info("En ConsultaDB: getEmpletadoExterno  obtiene"+
//	  empleadoExterno.toString()); if(!empleadoExterno.isEmpty()) { empExterno=
//	  empleadoExterno.get(0); }else { empExterno = new EmpleadoDto(); }
//	  
//	  return empExterno; 
//	  }
//	 
	
	//obtener url de tabla 
	public String getUrl() {
		//String urlRedirec = "";
		String parametro = "url_redirect_prod";
		String sqlconsultaUrl =   "SELECT VALOR FROM HUMAN.HU_CAT_PROPIEDADES WHERE PARAMETRO ="+ " '"+parametro+ "' ";
		List<HuCatPropiedadesDto>urlRedirec = jdbcTemplate.query(sqlconsultaUrl,BeanPropertyRowMapper.newInstance(HuCatPropiedadesDto.class));
		LOG.info(sqlconsultaUrl);
		LOG.info(urlRedirec.get(0).getValor());
		return urlRedirec.get(0).getValor();
	}
	
	
	
	
	
	
	//accion para numero de empleado con menos de 10 digitos solo en HU_USUARIO_EXTERNO
	public String completaNumEmp(Long numEmpleado) {
				String numEStr= String.valueOf(numEmpleado);
				int faltantes = 10-numEStr.length();
				String  numEmplComp = "" ;
				if(numEStr.length()<10) {
					for(int i = 0; i<faltantes; i++) {
						numEmplComp = numEmplComp +'0';
					}
					numEmplComp= numEmplComp+numEStr;
					
				}else {
					numEmplComp=numEStr;
				}
				LOG.info("En completaNumEmp, numEmplComp= " +numEmplComp);
				return numEmplComp;
	}
	
	//Obtener datos empleado interno  por email
	public EmpleadoDto getEmpleado (String correo){
		String sqlNumEmplClaveComp = "SELECT he.NUM_CIA, he.NUM_EMP,he.APELL_PAT,he.APELL_MAT,he.NOMBRE, hc.NUMERO_COMPANIA, hc.CLAVE_COMPANIA \r\n"
				+ "            FROM HUMAN.HU_EMPLS he, HUMAN.HU_COMPANIA hc \r\n"
				+ "            WHERE he.NUM_CIA = hc.NUMERO_COMPANIA AND he.STATUS = \r\n"
				+ "            'A' AND NUMERO_COMPANIA < 5000 AND LOWER (TRIM( he.EMAIL)) ="+" '"+ correo +"' ";
	
		EmpleadoDto emp =  null;
		List<EmpleadoDto> empleadoDtoNuevo = jdbcTemplate.query(sqlNumEmplClaveComp,BeanPropertyRowMapper.newInstance(EmpleadoDto.class));
		LOG.info("En ConsultaDB: getEmpleado correo  obtiene"+ empleadoDtoNuevo.toString());
		if(!empleadoDtoNuevo.isEmpty()) {
			emp =  empleadoDtoNuevo.get(0);
			
		}else {
			 emp = new EmpleadoDto();
			
	}
		return emp;//trae datos completos(nombre y apellidos)
	}
	
	
	//Obtener datos empleado interno  por email	
	public EmpleadoDto getEmpletadoExterno(String correo){
		
		
		
				String sqlEmpleadosExternos ="SELECT substr(EXT.USER_SKEY,1,(length(EXT.USER_SKEY)-10)) AS NUM_CIA,\r\n"
						+ "				substr(EXT.USER_SKEY, length(substr(EXT.USER_SKEY,1,(length(EXT.USER_SKEY)-10)))+1 , (length(EXT.USER_SKEY))) AS NUM_EMP,\r\n"
						+ "				substr(EXT.USER_SKEY,1,(length(EXT.USER_SKEY)-10)) AS NUMERO_COMPANIA, \r\n"
						+ "				(SELECT CLAVE_COMPANIA FROM HUMAN.HU_COMPANIA WHERE NUMERO_COMPANIA = substr(EXT.USER_SKEY,1,(length(EXT.USER_SKEY)-10))) AS CLAVE_COMPANIA, \r\n"
						+ "              APELL_PAT, APELL_MAT, NOMBRE \r\n"	
						+ "				FROM HUMAN.HU_USUARIO_EXTERNO EXT \r\n"
						+ "				WHERE  STATUS = 'A' \r\n"
						+ "				AND substr(EXT.USER_SKEY,1,(length(EXT.USER_SKEY)-10)) < 5000 AND LOWER (TRIM(EMAIL)) ="+" '"+correo+"' "; 
				EmpleadoDto empExterno = null;
				List<EmpleadoDto> empleadoExterno =  jdbcTemplate.query(sqlEmpleadosExternos,BeanPropertyRowMapper.newInstance(EmpleadoDto.class));
				LOG.info("En ConsultaDB: getEmpletadoExterno  obtiene"+ empleadoExterno.toString());
				if(!empleadoExterno.isEmpty()) {
					empExterno= empleadoExterno.get(0);
				}
//				else {
//					empExterno =  new EmpleadoDto();
//				}
				
				return empExterno;
	}
	
	

}
