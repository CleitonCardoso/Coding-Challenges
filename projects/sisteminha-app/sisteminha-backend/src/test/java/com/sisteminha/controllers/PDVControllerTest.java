package com.sisteminha.controllers;

import java.util.Date;

import javax.transaction.Transactional;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sisteminha.entities.PDV;
import com.sisteminha.entities.PDV.PDVBuilder;
import com.sisteminha.repositories.PDVRepository;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class PDVControllerTest {

	@Autowired
	private PDVController pdvController;

	@Autowired
	private PDVRepository pdvRepository;

	@Test
	public void save() {
		PDV pdv = PDV.builder()
				.name( "PDV 1" )
				.address( "Rua dos PDVS" )
				.phone( "08004444001" )
				.startingDate( new Date() )
				.endingDate( new Date() )
				.build();

		PDV pdvSavedByController = pdvController.save( pdv );

		PDV pdvFound = pdvRepository.findOne( pdvSavedByController.getId() );

		Assert.assertThat( pdvFound.getId(), Matchers.equalTo( pdvSavedByController.getId() ) );
		Assert.assertThat( pdvFound.getName(), Matchers.equalTo( pdv.getName() ) );
		Assert.assertThat( pdvFound.getAddress(), Matchers.equalTo( pdv.getAddress() ) );
		Assert.assertThat( pdvFound.getPhone(), Matchers.equalTo( pdv.getPhone() ) );
		Assert.assertThat( pdvFound.getStartingDate().getTime(), Matchers.equalTo( pdv.getStartingDate().getTime() ) );
		Assert.assertThat( pdvFound.getEndingDate().getTime(), Matchers.equalTo( pdv.getEndingDate().getTime() ) );
	}

	@Test
	public void find() {
		PDV pdv = PDV.builder()
				.name( "PDV 2" )
				.address( "Rua dos PDVS" )
				.phone( "08004444001" )
				.startingDate( new Date() )
				.endingDate( new Date() )
				.build();

		PDV pdvSavedByRepository = pdvRepository.save( pdv );

		PDV pdvFound = pdvController.find( pdvSavedByRepository.getId() );

		Assert.assertThat( pdvFound.getId(), Matchers.equalTo( pdvSavedByRepository.getId() ) );
		Assert.assertThat( pdvFound.getName(), Matchers.equalTo( pdv.getName() ) );
		Assert.assertThat( pdvFound.getAddress(), Matchers.equalTo( pdv.getAddress() ) );
		Assert.assertThat( pdvFound.getPhone(), Matchers.equalTo( pdv.getPhone() ) );
		Assert.assertThat( pdvFound.getStartingDate().getTime(), Matchers.equalTo( pdv.getStartingDate().getTime() ) );
		Assert.assertThat( pdvFound.getEndingDate().getTime(), Matchers.equalTo( pdv.getEndingDate().getTime() ) );
	}

	@Test
	public void update() {
		String firstAddress = "Rua dos PDVS";

		PDV pdv = PDV.builder()
				.name( "PDV 3" )
				.address( firstAddress )
				.phone( "08004444001" )
				.startingDate( new Date() )
				.endingDate( new Date() )
				.build();

		PDV pdvSaved = pdvRepository.save( pdv );

		pdvSaved.setAddress( "Outro endereço" );

		pdvController.update( pdvSaved );

		PDV pdvFound = pdvController.find( pdvSaved.getId() );

		Assert.assertThat( pdvFound.getId(), Matchers.equalTo( pdvSaved.getId() ) );
		Assert.assertThat( pdvFound.getName(), Matchers.equalTo( pdv.getName() ) );
		Assert.assertThat( pdvFound.getPhone(), Matchers.equalTo( pdv.getPhone() ) );
		Assert.assertThat( pdvFound.getStartingDate().getTime(), Matchers.equalTo( pdv.getStartingDate().getTime() ) );
		Assert.assertThat( pdvFound.getEndingDate().getTime(), Matchers.equalTo( pdv.getEndingDate().getTime() ) );
		Assert.assertThat( pdvFound.getAddress(), Matchers.not( firstAddress ) );
	}

	@Test
	public void delete() {
		PDV pdv = PDV.builder()
				.name( "PDV 4" )
				.address( "Rua dos PDVS" )
				.phone( "08004444001" )
				.startingDate( new Date() )
				.endingDate( new Date() )
				.build();

		PDV pdvSaved = pdvRepository.save( pdv );

		pdvController.delete( pdvSaved.getId() );

		PDV pdvFound = pdvRepository.findOne( pdvSaved.getId() );

		Assert.assertThat( pdvFound, Matchers.nullValue() );
	}

	@Test
	public void list() {
		PDVBuilder pdvBuilder = PDV.builder()
				.address( "Rua dos PDVS" )
				.phone( "08004444001" )
				.startingDate( new Date() )
				.endingDate( new Date() );

		pdvRepository.save( pdvBuilder.name( "PDV 01" ).build() );
		pdvRepository.save( pdvBuilder.name( "PDV 02" ).build() );

		Iterable<PDV> pdvs = pdvController.list();

		Assert.assertThat( pdvs.spliterator().getExactSizeIfKnown(), Matchers.equalTo( 2L ) );
	}

}
