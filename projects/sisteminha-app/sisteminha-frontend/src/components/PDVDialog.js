import React from 'react';
import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';
import TextField from 'material-ui/TextField';
import axios from 'axios';
import TimePicker from 'material-ui/TimePicker';
import InputMask from 'react-input-mask';

export default class PDVDialog extends React.Component {

  constructor(props) {
    super(props)
    this.handleClose = this.handleClose.bind(this)
  }
  state = {
    open: false,
    pdv: {
    }
  };

  handleOpen = (pdv, credentials) => {
    if (!pdv)
      pdv = {};
    this.setState({ open: true, pdv: pdv, credentials: credentials });
  };

  handleClose = (event) => {
    this.props.handler();
    this.setState({ open: false, pdv: {} });
  };

  handleSave = (event) => {
    event.preventDefault();

    axios({
      method: 'post',
      url: 'http://localhost:8080/pdv',
      headers: {
        'Content-Type': 'application/json',
      },
      auth: {
        username: this.state.credentials.username,
        password: this.state.credentials.password
      },
      data: this.state.pdv
    }).then((response) => {
      this.handleClose();
    }).catch((error) => {
      console.log(error);
    });

  };

  setValue = (event, date) => {
    var pdv = this.state.pdv;
    if (!event && date) {
      if (!pdv.startingDate) {
        pdv.startingDate = date.getTime();
      }
      else {
        pdv.endingDate = date.getTime();
      }
    } else {
      var field = event.target.id;
      var value = event.target.value;
      pdv[field] = value;
      console.log(pdv[field]);
    }
    this.setState({
      pdv
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
          title={this.state.pdv.id ? "Editar PDV" : "Novo PDV"}
          actions={actions}
          modal={false}
          open={this.state.open}
          onRequestClose={this.handleClose}
        >
          <TextField
            floatingLabelText="Nome"
            fullWidth={true}
            onChange={this.setValue}
            id="name"
            value={this.state.pdv.name}
          /><br />
          <TextField
            floatingLabelText="Telefone"
            fullWidth={true}
            onChange={this.setValue}
            id="phone"
          >
            <InputMask mask="(99) 999999999" maskChar=" " value={this.state.pdv.phone} />
          </TextField>
          <br />
          <TextField
            floatingLabelText="Endereço"
            fullWidth={true}
            onChange={this.setValue}
            value={this.state.pdv.address}
            id="address"
          />
          <br />
          <TimePicker
            format="24hr"
            floatingLabelText="Horário Início"
            onChange={this.setValue}
            id="startingDate"
            value={this.state.pdv.startingDate ? new Date(this.state.pdv.startingDate) : null}
          />
          <br />
          <TimePicker
            format="24hr"
            floatingLabelText="Horário Fim"
            onChange={this.setValue}
            disabled={!this.state.pdv.startingDate}
            value={this.state.pdv.endingDate ? new Date(this.state.pdv.endingDate) : null}
            id="endingDate"
          />
        </Dialog>
      </div>
    );
  }
}