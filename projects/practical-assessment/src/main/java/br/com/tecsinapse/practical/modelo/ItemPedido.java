package br.com.tecsinapse.practical.modelo;

import java.math.BigDecimal;

public class ItemPedido {
    private String cnpjCliente;
    private String usuarioSolicitante;
    private String codigoItem;
    private int quantidade = 0;
    private BigDecimal valorTotal = BigDecimal.ZERO;
    private BigDecimal valorUnitario = BigDecimal.ZERO;

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

    public String getCodigoItem() {
        return this.codigoItem;
    }

    public void setCodigoItem(final String codigoItem) {
        this.codigoItem = codigoItem;
    }

    public int getQuantidade() {
        return this.quantidade;
    }

    public void setQuantidade(final int quantidade) {
        this.quantidade = quantidade;
        this.atualizarValorUnitario();
    }

    public BigDecimal getValorTotal() {
        return this.valorTotal;
    }

    public void setValorTotal(final BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
        this.atualizarValorUnitario();
    }

    public BigDecimal getValorUnitario() {
        return this.valorUnitario;
    }

    protected final void setValorUnitario(final BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private final void atualizarValorUnitario() {
        if (this.quantidade != 0) {
            this.setValorUnitario(this.valorTotal.divide(new BigDecimal(this.quantidade)));
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this.getClass().equals(obj.getClass())) {
            final ItemPedido other = (ItemPedido) obj;
            return this.getCodigoItem().equals(other.getCodigoItem());
        }
        return super.equals(obj);
    }

}
