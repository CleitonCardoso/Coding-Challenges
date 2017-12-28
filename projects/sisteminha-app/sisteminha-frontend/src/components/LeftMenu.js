import React from 'react';
import Paper from 'material-ui/Paper';
import Menu from 'material-ui/Menu';
import MenuItem from 'material-ui/MenuItem';
import People from 'material-ui/svg-icons/social/people';
import Store from 'material-ui/svg-icons/action/store';
import { withRouter } from 'react-router-dom';


class LeftMenu extends React.Component {
    constructor(props) {
        super(props)
        this.state = props.location.state
    }

    redirect = (pathname) => {
        this.props.history.push({
            pathname: pathname,
            state: this.state
        })
    }



    render() {
        return (
            <div style={style}>
                <Paper >
                    <Menu>
                        <MenuItem style={itemButton} primaryText="PDVs" leftIcon={<Store />} onClick={() => this.redirect('/pdvs')} />
                        <MenuItem style={itemButton} primaryText="Usuários" leftIcon={<People />} onClick={() => this.redirect('/users')} />
                    </Menu>
                </Paper>
            </div>)
    }
}

export default withRouter(LeftMenu)

const style = {
    position: 'inline-block',
}

const itemButton = {
    width: '11.3em',
    overflow: 'hidden'
}