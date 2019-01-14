import React, { Component } from 'react'
import { Link } from 'react-router-dom'
import {
  Button,
  Container,
  Grid,
  Header,
  Image,
  Item,
  Menu,
  Segment,
  Dropdown,
  Modal,
  Message
} from 'semantic-ui-react'
import 'semantic-ui-css/semantic.min.css'
import './App.css'
import DealService from './services/DealService'

const dealService = new DealService()

class App extends Component {
  state = {
    deals: undefined
  }

  componentWillMount = () => {
    this.loadContent()
  }

  loadContent = () => {
    dealService.listAll(response => {
      this.setState({ deals: response.data })
    })
  }

  clickToBuy = (event, data) => {
    this.setState({
      openSaveConfirmation: true,
      selectedBuyOption: data.value
    })
  }

  closeConfirmation = (event, data) => {
    this.setState({
      openSaveConfirmation: false
    })
  }

  confirmSave = (event, data) => {
    dealService.confirmSale(this.state.selectedBuyOption, () => {
      this.loadContent()
      this.closeConfirmation()
    })
  }

  render() {
    return (
      <div className='container'>
        <header>
          <div className='header-content'>
            <Image src={require('./logo.svg')} floated='left' />
            <Button primary floated='right' as={Link} to='/admin'>Admin</Button>
          </div>
        </header>
        <main>
          <div>
            <Container>
              <Grid columns={2} doubling stackable>
                {this.state.deals && this.state.deals.map((deal, index) =>
                  <Grid.Column key={index}>
                    <Segment>
                      <Item.Group divided>
                        <Item>
                          <Item.Image src={require('./wireframe.png')} />
                          <Item.Content>
                            <Item.Header as='a'>{deal.title}</Item.Header>
                            <Item.Meta>
                              <span>{deal.totalSold} vendidos</span>
                            </Item.Meta>
                            <Item.Description>
                              {deal.text}
                            </Item.Description>
                            <Item.Extra>
                              <Menu compact floated='right' >
                                <Dropdown text='Comprar' options={deal.buyOptions && deal.buyOptions.map((buyOption, index) =>
                                  ({ key: index, text: buyOption.title, value: buyOption.id })
                                )}
                                  icon='dollar sign'
                                  disabled={!(deal.buyOptions && deal.buyOptions.length)}
                                  onChange={this.clickToBuy}
                                  simple item
                                />
                              </Menu>
                            </Item.Extra>
                          </Item.Content>
                        </Item>
                      </Item.Group>
                    </Segment>
                  </Grid.Column>
                )}
              </Grid>
              <Modal open={this.state.openSaveConfirmation} style={{ width: '20%' }}>
                <Modal.Content >
                  <Header size='small' color='black'> Confirmar compra? </Header>
                  <Message positive color='red' error visible={false} hidden >
                    <Message.Header>Algo de errado aconteceu :(</Message.Header>
                    <p>
                      {this.state.errorMessage}
                    </p>
                  </Message>
                </Modal.Content>
                <Modal.Actions>
                  <Button onClick={this.closeConfirmation} negative>Cancelar</Button>
                  <Button onClick={this.confirmSave} positive >Confirmar</Button>
                </Modal.Actions>
              </Modal>
            </Container>
          </div>
        </main>
        <footer> <Header size='small' color='black'>Desenvolvido por <a href='http://cleitoncardoso.github.io/' target='_blank' rel='noopener noreferrer'>Cleiton Cardoso </a></Header></footer>
      </div>
    );
  }
}

export default App;
