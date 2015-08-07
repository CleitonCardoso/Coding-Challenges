package br.com.tecsinapse.practical.teste;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import br.com.tecsinapse.practical.main.Main;
import br.com.tecsinapse.practical.modelo.ItemPedido;
import br.com.tecsinapse.practical.modelo.Pedido;

import com.google.common.collect.Lists;

public class Teste {

    @DataProvider(name = "itens")
    public Object[][] itens() {
        final ItemPedido ip1 = this.gerarItemPedido("1", "1", "1", 1);
        final ItemPedido ip2 = this.gerarItemPedido("2", "2", "2", 2);
        final ItemPedido ip3 = this.gerarItemPedido("1", "1", "3", 3);
        final ItemPedido ip4 = this.gerarItemPedido("1", "1", "3", 3);
        final ItemPedido ip5 = this.gerarItemPedido("1", "1", "3", 6);

        final Pedido p1 = this.gerarPedido(ip1.getCnpjCliente(), ip1.getUsuarioSolicitante(), ip1, ip3);

        final Pedido p2 = this.gerarPedido(ip2.getCnpjCliente(), ip2.getUsuarioSolicitante(), ip2);

        final Pedido p3 = this.gerarPedido(ip1.getCnpjCliente(), ip1.getUsuarioSolicitante(), ip1, ip5);

        return new Object[][]{{Lists.newArrayList(ip1, ip2), Lists.newArrayList(p1, p2)}, {Lists.newArrayList(), Lists.newArrayList()}, {Lists.newArrayList(ip1, ip3, ip4), Lists.newArrayList(p3)}};
    }

    @Test(dataProvider = "itens")
    public void gerarPedidosAgrupados(final List<ItemPedido> itens, final List<Pedido> pedidosEsperados) {
        final List<Pedido> pedidos = Main.gerarPedidosAgrupados(itens);

        Assert.assertEquals(pedidos.size(), pedidosEsperados.size(), "número diferente de pedidos");

        for (final Pedido pEsperado : pedidosEsperados) {
            boolean achouPedidoEsperado = false;
            for (final Pedido p : pedidos) {
                if (pEsperado.getCnpjCliente().equals(p.getCnpjCliente()) && pEsperado.getUsuarioSolicitante().equals(p.getUsuarioSolicitante())) {

                    for (final ItemPedido ipEsperado : pEsperado.getItens()) {
                        boolean achouItemPedidoEsperado = false;
                        for (final ItemPedido ip : p.getItens()) {
                            if (ipEsperado.getCnpjCliente().equals(ip.getCnpjCliente()) && ipEsperado.getCodigoItem().equals(ip.getCodigoItem()) && ipEsperado.getUsuarioSolicitante().equals(ip.getUsuarioSolicitante()) && ipEsperado.getQuantidade() == ip.getQuantidade()) {
                                Assert.assertFalse(achouItemPedidoEsperado, "achou 2 vezes o item: " + ipEsperado.getCodigoItem());
                                achouItemPedidoEsperado = true;
                            }
                        }
                        if (achouItemPedidoEsperado) {
                            break;
                        }

                    }

                    Assert.assertFalse(achouPedidoEsperado, "achou 2 vezes o pedido do cnpj: " + pEsperado.getCnpjCliente() + " e usuario: " + pEsperado.getUsuarioSolicitante());
                    achouPedidoEsperado = true;
                }

                if (achouPedidoEsperado) {
                    break;
                }
            }
        }

    }

    public ItemPedido gerarItemPedido(final String cnpjCliente, final String usuarioSolicitante, final String codigoItem, final int quantidade) {
        final ItemPedido ip = new ItemPedido();
        ip.setCnpjCliente(cnpjCliente);
        ip.setUsuarioSolicitante(usuarioSolicitante);
        ip.setCodigoItem(codigoItem);
        ip.setQuantidade(quantidade);
        return ip;
    }

    public Pedido gerarPedido(final String cnpjCliente, final String usuarioSolicitante, final ItemPedido... itens) {
        final Pedido p = new Pedido();
        p.setCnpjCliente(cnpjCliente);
        p.setUsuarioSolicitante(usuarioSolicitante);
        if (itens != null) {
            p.getItens().addAll(Lists.newArrayList(itens));
        }
        return p;

    }

}
