package br.com.aula;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import br.com.aula.exception.ContaInvalidaException;
import br.com.aula.exception.ContaJaExistenteException;
import br.com.aula.exception.ContaNaoExistenteException;
import br.com.aula.exception.ContaSemSaldoException;
import br.com.aula.exception.TransferenciaNegativaException;

public class BancoTest {

	@Test
	public void deveCadastrarConta() throws ContaJaExistenteException, ContaInvalidaException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta = new Conta(cliente, 123, 0, TipoConta.CORRENTE);
		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta);

		// Verificação
		assertEquals(1, banco.obterContas().size());
	}

	@Test(expected = ContaJaExistenteException.class)
	public void naoDeveCadastrarContaNumeroRepetido() throws ContaJaExistenteException, ContaInvalidaException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta1 = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta conta2 = new Conta(cliente2, 123, 0, TipoConta.POUPANCA);

		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta1);
		banco.cadastrarConta(conta2);

		Assert.fail();
	}
	
	@Test(expected = ContaInvalidaException.class)
	public void naoDeveCadastrarContaComNumeroInvalido() throws ContaJaExistenteException, ContaInvalidaException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta1 = new Conta(cliente, 10001, 0, TipoConta.CORRENTE);
		
		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta1);

		Assert.fail();
	}
	
	@Test(expected = ContaJaExistenteException.class)
	public void naoDeveCadastrarContaNomeRepetido() throws ContaJaExistenteException, ContaInvalidaException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta1 = new Conta(cliente, 321, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Joao");
		Conta conta2 = new Conta(cliente2, 123, 0, TipoConta.POUPANCA);

		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta1);
		banco.cadastrarConta(conta2);

		Assert.fail();
	}

	@Test
	public void deveEfetuarTransferenciaContasCorrentes() throws ContaSemSaldoException, ContaNaoExistenteException, TransferenciaNegativaException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// Ação
		banco.efetuarTransferencia(123, 456, 100);

		// Verificação
		assertEquals(-100, contaOrigem.getSaldo());
		assertEquals(100, contaDestino.getSaldo());
	}
	
	@Test
	public void deveEfetuarTransferenciaContasPoupanca() throws ContaSemSaldoException, ContaNaoExistenteException, TransferenciaNegativaException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 100, TipoConta.POUPANCA);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 100, TipoConta.POUPANCA);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// Ação
		banco.efetuarTransferencia(123, 456, 100);

		// Verificação
		assertEquals(0, contaOrigem.getSaldo());
		assertEquals(200, contaDestino.getSaldo());
	}
	
	@Test(expected = ContaNaoExistenteException.class)
	public void verificarContaDeOrigem() throws ContaNaoExistenteException, ContaSemSaldoException, TransferenciaNegativaException {
		Cliente cliente = new Cliente("Escobar");
		Conta contaOrigem = new Conta(cliente, 234, 0, TipoConta.CORRENTE);
		
		Cliente cliente2 = new Cliente("Marietta");
		Conta contaDestino = new Conta(cliente2, 432, 0, TipoConta.CORRENTE);
		
		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));
		
		banco.efetuarTransferencia(999, 432, 50);
		
		Assert.fail();	
	}
	
	@Test(expected = ContaSemSaldoException.class)
	public void contaPoupancaNaoDeveTerSaldoNegativo() throws ContaSemSaldoException, ContaNaoExistenteException, TransferenciaNegativaException {
		Cliente cliente = new Cliente("Jose");
		Conta contaOrigem = new Conta(cliente, 567, 0, TipoConta.POUPANCA);
		
		Cliente cliente2 = new Cliente("Andrade");
		Conta contaDestino = new Conta(cliente2, 765, 0, TipoConta.CORRENTE);
		
		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));
		
		banco.efetuarTransferencia(567, 765, 100);
		
		Assert.fail();
	}
	
	@Test(expected = ContaNaoExistenteException.class)
	public void verificarContaDeDestino() throws ContaNaoExistenteException, ContaSemSaldoException, TransferenciaNegativaException {
		Cliente cliente = new Cliente("Escobar");
		Conta contaOrigem = new Conta(cliente, 234, 0, TipoConta.CORRENTE);
		
		Cliente cliente2 = new Cliente("Marietta");
		Conta contaDestino = new Conta(cliente2, 432, 0, TipoConta.CORRENTE);
		
		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));
		
		banco.efetuarTransferencia(234, 999, 50);
		
		Assert.fail();	
	}
	
	@Test(expected = TransferenciaNegativaException.class)
	public void naoDeveTransferirValorNegativo() throws TransferenciaNegativaException, ContaNaoExistenteException, ContaSemSaldoException {
		Cliente cliente = new Cliente("Peixoto");
		Conta contaOrigem = new Conta(cliente, 123, 0, TipoConta.CORRENTE);
		
		Cliente cliente2 = new Cliente("Carolina");
		Conta contaDestino = new Conta(cliente2, 321, 0, TipoConta.CORRENTE);
		
		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));
		
		banco.efetuarTransferencia(123, 321, -50);
		
		Assert.fail();
	}
}
