/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.feryla.webapptemplate.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import net.feryla.webapptemplate.factory.VoteringFactory;
import net.feryla.webapptemplate.factory.VoteringPartySummaryFactory;
import net.feryla.webapptemplate.models.Votering;
import net.feryla.webapptemplate.models.*;

/**
 *
 * @author sondre
 */
@Path("/votering")
@Produces(MediaType.APPLICATION_JSON)
public class Votes {

    @GET
    @Path("/{id}")

    public List<Votering> getVote(@PathParam("id") Integer id) throws IOException {
        return new VoteringFactory().getVotering(id);
    }

    @GET
    @Path("/{id}/summary")
    public VoteringSummary getVoteSummary(@PathParam("id") Integer id) throws IOException {

        List<Votering> votering = new VoteringFactory().getVotering(id);

        VoteringSummary vtSummary = new VoteringSummary();

        votering.stream().forEach(p -> vtSummary.add(p.getVote()));

        return vtSummary;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Path("/{id}/partySummary")
    public List<VoteringPartySummary> getVoteSummaryPerParty(@PathParam("id") Integer id) throws IOException {

        List<Votering> votering = new VoteringFactory().getVotering(id);
        
        return new VoteringPartySummaryFactory().getVoteringPartySummery(votering);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Path("/{id}/genderSummary")
    public List<VoteringPartySummary> getVoteSummaryPerGender(@PathParam("id") Integer id) throws IOException {

        List<Votering> votering = new VoteringFactory().getVotering(id);
       
        List<VoteringPartySummary> genderSummaryList = new ArrayList<>();
        
        VoteringPartySummary boysSummary = new VoteringPartySummary();
        boysSummary.setGender(2);
        boysSummary.setPartyCode("Mann");
        VoteringPartySummary girslSummary = new VoteringPartySummary();
        girslSummary.setPartyCode("Kvinne");

        girslSummary.setGender(1);
        
        votering.stream().forEach((vt) -> {
            //2-man, 1-lady
            Integer gender =  vt.getRep().getGender();
            if(gender == 2){
                boysSummary.add(vt.getVote());
            }else if(gender == 1){
                girslSummary.add(vt.getVote());
            }
        });
        genderSummaryList.add(boysSummary);
        genderSummaryList.add(girslSummary);
        
        return genderSummaryList;
    }
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Path("/{id}/fylkeSummary")
    public List<VoteringPartySummary> getVoteSummaryPerFylke(@PathParam("id") Integer id) throws IOException {

        List<Votering> votering = new VoteringFactory().getVotering(id);
        
        List<VoteringPartySummary> voteringPartySummaryList = new ArrayList<>();
        
        //PartyName, Votering
        Map<String,List<Votering>> vMap = new HashMap<>();
        
        votering.stream().forEach((vot) -> {
            String fylkeRep = vot.getRep().getFylke();
            List<Votering> vList;
            if(vMap.get(fylkeRep) != null){
                vList = vMap.get(fylkeRep);
                vList.add(vot);
                
            }else{
                vList = new ArrayList<>();
                vList.add(vot);
            }
            vMap.put(fylkeRep, vList);
        });
        
        vMap.entrySet().stream().map((entry) -> {
            return entry;
        }).map((entry) -> {
            VoteringPartySummary vtPs = new VoteringPartySummary();
            vtPs.setPartyCode(entry.getKey());
            for(Votering v : entry.getValue()){
                vtPs.add(v.getVote());
            }
            return vtPs;
        }).forEach((vtPs) -> {
            voteringPartySummaryList.add(vtPs);
        });
      return voteringPartySummaryList;

    }
    
    @GET
    @Path("/sak/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SaksVotering getSaksVotering(@PathParam("id") String saksId) throws IOException {
        
        SaksVotering s = new VoteringFactory().getVoteringForSak(saksId);
        
//        SaksVotering s = new SaksVotering();
//        s.setVoteringDescription("Det voteres om blabla for bla");
//        s.setVoteringSummaryList(new ArrayList<>());
//        s.getVoteringSummaryList().add(new VoteringSummary(40, 60, 69));
//        s.getVoteringSummaryList().add(new VoteringSummary(90, 60, 19));
        return s;
    }
    
    
    

}
