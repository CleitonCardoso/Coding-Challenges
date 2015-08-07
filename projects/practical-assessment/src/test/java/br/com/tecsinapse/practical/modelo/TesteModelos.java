/**
 *
 */
package br.com.tecsinapse.practical.modelo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import br.com.tecsinapse.practical.main.Main;

/**
 * @author cleiton.cardoso
 *
 */
public class TesteModelos {
    @Test
    public void calcularValorUnitarioAtribuindoQuantidadePrimeiro() {
        final ItemPedido itemPedido = new ItemPedido();
        itemPedido.setQuantidade(4);
        itemPedido.setValorTotal(new BigDecimal(20));
        Assert.assertEquals(new BigDecimal("5.00"), itemPedido.getValorUnitario());
    }

    @Test
    public void calcularValorUnitarioAtribuindoValorTotalPrimeiro() {
        final ItemPedido itemPedido = new ItemPedido();
        itemPedido.setValorTotal(new BigDecimal(20));
        itemPedido.setQuantidade(4);
        Assert.assertEquals(new BigDecimal("5.00"), itemPedido.getValorUnitario());
    }

    @Test
    public void arredondarValorNoSetValorUnitario() {
        final ItemPedido itemPedido = new ItemPedido();

        BigDecimal valor = new BigDecimal("1.789"); // 1.79
        itemPedido.setValorUnitario(valor);
        Assert.assertEquals(new BigDecimal("1.79"), itemPedido.getValorUnitario());

        valor = new BigDecimal("1.99854"); // 2.00
        itemPedido.setValorUnitario(valor);
        Assert.assertEquals(new BigDecimal("2.00"), itemPedido.getValorUnitario());

        valor = new BigDecimal("2.1068"); // 2.11
        itemPedido.setValorUnitario(valor);
        Assert.assertEquals(new BigDecimal("2.11"), itemPedido.getValorUnitario());

        valor = new BigDecimal("10.364"); // 10.36
        itemPedido.setValorUnitario(valor);
        Assert.assertEquals(new BigDecimal("10.36"), itemPedido.getValorUnitario());
    }

    @Test
    public void calcularValorTotalDoPedido() {
        final ItemPedido itemPedido = new ItemPedido();
        itemPedido.setValorTotal(new BigDecimal("15"));
        itemPedido.setQuantidade(3);

        final ItemPedido itemPedido2 = new ItemPedido();
        itemPedido2.setValorTotal(new BigDecimal("40"));
        itemPedido2.setQuantidade(4);

        //valor total = 55

        final Pedido pedido = new Pedido();
        pedido.setItens(Arrays.asList(itemPedido, itemPedido2));

        Assert.assertEquals(new BigDecimal(55), pedido.getValorTotal());
    }

    @Test
    public void testaComparaItensPeloCodigo() {

        final Pedido pedido = new Pedido();
        final ItemPedido itemPedido = new ItemPedido();
        itemPedido.setCodigoItem("1");

        pedido.getItens().add(itemPedido);
        final ItemPedido itemPedido2 = new ItemPedido();
        itemPedido2.setCodigoItem("1");

        Assert.assertTrue(itemPedido.equals(itemPedido2));
        Assert.assertTrue(pedido.getItens().contains(itemPedido2));
    }

    @Test
    public void gerarPedidosAgrupados() {
        final ItemPedido item = new ItemPedido();
        item.setCodigoItem("1");
        item.setCnpjCliente("1");
        item.setUsuarioSolicitante("1");
        item.setQuantidade(1);
        item.setValorTotal(new BigDecimal(1));

        final ItemPedido item2 = new ItemPedido();
        item2.setCodigoItem("1");
        item2.setCnpjCliente("1");
        item2.setUsuarioSolicitante("1");
        item2.setQuantidade(1);
        item2.setValorTotal(new BigDecimal(1));

        // tem que ter apenas 1 grupo;
        final List<Pedido> grupo = Main.gerarPedidosAgrupados(Arrays.asList(item, item2));
        Assert.assertTrue(grupo.size() == 1);

        // tem que ter no mínimo uma instância de pedido;
        final Pedido pedido = grupo.get(0);
        Assert.assertNotNull(pedido);

        // Pedido deve ter apenas 1 item na lista, com os valores quantidade = 2 e valortotal = 2.00
        final List<ItemPedido> itens = pedido.getItens();
        Assert.assertTrue(itens.size() == 1);

        final ItemPedido itemPedido = itens.get(0);
        Assert.assertEquals(2, itemPedido.getQuantidade());
        Assert.assertEquals(new BigDecimal(2.00), itemPedido.getValorTotal());
    }
}
