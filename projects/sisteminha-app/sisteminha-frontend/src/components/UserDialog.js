import React from 'react';
import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';
import TextField from 'material-ui/TextField';
import axios from 'axios';
import Toggle from 'material-ui/Toggle';

export default class UserDialog extends React.Component {

  constructor(props) {
    super(props)
    this.handleClose = this.handleClose.bind(this)
    this.state = {
      open: false,
      user: {
      }
    }
  }

  handleOpen = (user, credentials) => {
    if (!user)
      user = {};
    this.setState({ open: true, user: user, credentials: credentials });
  };

  handleClose = (event) => {
    this.props.handler();
    this.setState({ open: false, user: {} });
  };

  handleSave = (event) => {
    event.preventDefault();

    axios({
      method: 'post',
      url: 'http://localhost:8080/user',
      headers: {
        'Content-Type': 'application/json',
      },
      auth: {
        username: this.state.credentials.username,
        password: this.state.credentials.password
      },
      data: this.state.user
    }).then((response) => {
      this.handleClose();
    }).catch((error) => {
      console.log(error);
    });

  };

  setValue = (event) => {
    var user = this.state.user;
    var field = event.target.id;
    var value = event.target.value;
    if (field === "active") {
      value = event.target.checked;
    }
    user[field] = value;
    this.setState({
      user
    })
  }

  render() {
    const actions = [
      <FlatButton
        label="Cancel"
        primary={true}
        onClick={this.handleClose} />,
      <FlatButton
        label="Submit"
        primary={true}
        keyboardFocused={true}
        onClick={this.handleSave} />,
    ];
    return (
      <div>
        <Dialog
          title={this.state.user.id ? "Editar Usuário" : "Novo Usuário"}
          actions={actions}
          modal={false}
          open={this.state.open}
          onRequestClose={this.handleClose}
        >
          <TextField
            floatingLabelText="Nome de usuário"
            fullWidth={true}
            onChange={this.setValue}
            id="username"
            value={this.state.user.username}
          /><br />
          <TextField
            floatingLabelText="Senha"
            fullWidth={true}
            onChange={this.setValue}
            id="password"
            type="password"
          />
          <br />
          <br />
          <Toggle
            label="Ativo"
            defaultToggled={this.state.user.active}
            onToggle={this.setValue}
            id="active"
          />
        </Dialog>
      </div >
    );
  }
}