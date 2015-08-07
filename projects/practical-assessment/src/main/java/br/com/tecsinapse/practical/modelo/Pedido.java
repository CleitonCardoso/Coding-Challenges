package br.com.tecsinapse.practical.modelo;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class Pedido {
    private String cnpjCliente;
    private String usuarioSolicitante;
    private List<ItemPedido> itens = new LinkedList<>();
    private BigDecimal valorTotal = BigDecimal.ZERO;

    public String getCnpjCliente() {
        return this.cnpjCliente;
    }

    public void setCnpjCliente(final String cnpjCliente) {
        this.cnpjCliente = cnpjCliente;
    }

    public String getUsuarioSolicitante() {
        return this.usuarioSolicitante;
    }

    public void setUsuarioSolicitante(final String usuarioSolicitante) {
        this.usuarioSolicitante = usuarioSolicitante;
    }

    public List<ItemPedido> getItens() {
        return this.itens;
    }

    public void setItens(final List<ItemPedido> itens) {
        this.itens = itens;
        this.atualizarValorTotal();
    }

    public BigDecimal getValorTotal() {
        return this.valorTotal;
    }

    protected void setValorTotal(final BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    /**
    *
    */
    protected final void atualizarValorTotal() {
        if (!this.itens.isEmpty()) {
            for (final ItemPedido itemPedido : this.itens) {
                this.valorTotal = this.valorTotal.add(itemPedido.getValorTotal());
            }
        }
    }
}
