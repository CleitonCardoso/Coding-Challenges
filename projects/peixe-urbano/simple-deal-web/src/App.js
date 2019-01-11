import React, { Component } from 'react'
import {
  Button,
  Container,
  Grid,
  Header,
  Icon,
  Image,
  Item,
  Label,
  Menu,
  Segment,
  Step,
  Table,
  Dropdown
} from 'semantic-ui-react'
import 'semantic-ui-css/semantic.min.css'
import './App.css'
import DealService from './services/dealService'

const dealService = new DealService()

class App extends Component {
  state = {
    deals: require('./deals.json')
  }

  componentWillMount = () => {
    dealService.listAll(response => {
      this.setState({ deals: response.data })
    })
  }

  clickToBuy = (event, data) => {
    console.log(data.options[data.value])
  }

  render() {
    return (
      <div className="container">
        <header>
          <Image src={require('./logo.svg')} centered />
          <Button />
        </header>
        <main>
          <div>
            <Container>
              <Grid columns={2} doubling stackable>

                {this.state.deals && this.state.deals.map((deal, index) =>
                  <Grid.Column key={index}>
                    <Segment>
                      <Item.Group divided>
                        <Item >
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
                                  ({ key: index, text: buyOption.title, value: index })
                                )}
                                  simple item icon='dollar sign' disabled={!(deal.buyOptions && deal.buyOptions.length)}
                                  onChange={this.clickToBuy}
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
            </Container>

          </div>
        </main>
        <footer> <Header size='small' color='grey'>Desenvolvido por <a href="http://cleitoncardoso.github.io/" target="_blank" rel="noopener noreferrer">Cleiton Cardoso </a></Header></footer>
      </div>
    );
  }
}

export default App;
