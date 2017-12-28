import React from 'react';

import './App.css';

export default class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isLogged: false
    }
  }

  switchScreens() {
    if (this.props.location.state && this.props.location.state.isLogged) {
      this.props.history.push({
        pathname: '/pdvs',
        state: {
          isLogged: true,
          credentials: this.props.location.state.credentials
        }
      })
    } else {
      this.props.history.push('/login');
    }
  }

  componentWillMount() {
    this.switchScreens();
  }

  render() {
    return (
      <div>
        <div className="routerView App">
          Algo deu errado e o app não carregou como deveria!
        </div>
      </div>
    );
  }
}
