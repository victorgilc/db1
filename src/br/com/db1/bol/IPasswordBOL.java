package br.com.db1.bol;

import br.com.db1.model.PasswordModel;

/**
 * Contrato para PasswordBOL
 * 
 * @author Victor Gil Coronel
 *
 */
public interface IPasswordBOL {
	/**
	 * Recebe a senha e devolve a força da mesma
	 * 
	 * @param password
	 * @return PasswordModel com a força da senha
	 */
	PasswordModel validate(String password);
}
