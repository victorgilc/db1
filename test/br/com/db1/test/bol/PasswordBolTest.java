package br.com.db1.test.bol;

import org.junit.Assert;
import org.junit.Test;

import br.com.db1.bol.core.PasswordBOL;
import br.com.db1.model.PasswordModel;

/**
 * 
 * Teste unitário para a "camada de negócios"
 * 
 * @author Victor Gil Coronel
 *
 */
public class PasswordBolTest {
	private PasswordBOL passwordBOL = new PasswordBOL();

	@Test
	public void trataItensSemelhantesDefaultsTest() {
		final String MY_PASS = "a123";
		final int MY_STRENGTH = 31;

		PasswordModel toReturn = passwordBOL.validate(MY_PASS);
		Assert.assertTrue(toReturn.getStrength().equals(MY_STRENGTH));
	}
}
