package main.java.BusinessLogic;

import main.java.DomainModel.*;
import main.java.ORM.*;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.ArrayList;

public class ManagerOwnerManagementController extends PersonController<Person>{

    protected ManagesDAO managesDAO = null;

    public ManagerOwnerManagementController(Person person) {
        super(person);
        managesDAO = new ManagesDAO();
    }

    public ManagerOwnerManagementController() {
        super(SessionController.getInstance().getPerson());
        managesDAO = new ManagesDAO();
    }

    public ManagerOwnerManagementController(Person person, UserDAO userDAO, GroupDAO groupDao, IsPartDAO isPartDao, WorkingHoursDAO workingHoursDAO, ReservationDAO reservationDao, InviteDAO inviteDao, FieldDAO fieldDao, ManagesDAO managesDAO, NotificationController notificationController) {
        super(person,userDAO,groupDao,isPartDao,workingHoursDAO,reservationDao,inviteDao,fieldDao,notificationController);
        this.managesDAO = managesDAO;
    }


    //methods

    public ArrayList<Field> getFieldsByFacility(Facility facility) throws SQLException {
        ArrayList<Field> fields;
        fields = fieldDao.getFieldsByFacility(facility.getId(), false);
        return fields;
    }

    public ArrayList<Reservation> getCurrentReservationsByField(int idField) throws SQLException, ClassNotFoundException {
        return filterByUpcomingReservations(reservationDao.getReservationsByField(idField), res -> res);
    }

    @Override
    protected String getProvinceForMatching(Field field){
        return field.getFacility().getProvince();
    }

    public ArrayList<WorkingHours> getWHsByFacilityByDay(int idFacility, DayOfWeek dayOfWeek) throws SQLException {
        ArrayList<WorkingHours> workingHours;
        workingHours = workingHoursDao.getWHsByFacility(idFacility);
        return workingHours;
    }

    public boolean reservationAnnouncement(String notificationMessage, Reservation reservation) {
        return notificationController.sendAnnouncements(reservation, notificationMessage) >= 0;
    }

    @Override
    public boolean joinGroupHelper(int idGroup, int guestUsers) throws SQLException, ClassNotFoundException{
        //managers and owners have not to join into group, then it is always true
        return true;
    }

    @Override
    public boolean removeGroupMembers(Group group, ArrayList<GroupMember> groupMembersDraftArray) {

        return false;

    }


    @Override
    public boolean applyChangesFromDraft(Group group, ArrayList<GroupMember> removedDraft, ArrayList<GroupMember> addedDraft, ArrayList<GroupMember> changedDraft, int ownGuestsSelected, ArrayList<String> inviteListDraft, User newGroupHead) throws SQLException, ClassNotFoundException {

        if (group != null) {

            int invitesSent = sendInvites(group,getUsersByUsernames(inviteListDraft));

            if (invitesSent < 0)
                return false;

            //removed
            if (removedDraft != null && !removedDraft.isEmpty()) {
                for (GroupMember groupMember : removedDraft) {
                    if (group.removeMember(groupMember.getUser())) {
                        if (!removeGroupMember(group.getReservation().getId(), groupMember.getUser().getId()))
                            return false;
                    }else
                        System.out.println("Error during removing member into group (local)");
                }

            }

            notificationController.connectObserverToReservation(group.getReservation());

            //added
            if (addedDraft != null && !addedDraft.isEmpty()) {
                for (GroupMember groupMember : addedDraft) {
                    if (group.addMember(groupMember.getUser(), groupMember.getOwnGuests())) {
                        if (!addGroupMember(group.getReservation().getId(), groupMember.getUser().getId(), groupMember.getOwnGuests()))
                            return false;
                    }else {
                        System.out.println("Error during adding member into group");
                    }
                }
            }

            //changed
            if (changedDraft != null && !changedDraft.isEmpty()) {
                for (GroupMember groupMember : changedDraft) {

                    if (group.changeUserGuests(groupMember.getUser().getUsername(), groupMember.getOwnGuests())) {
                        if (!changeUserGuests(group.getReservation().getId(), groupMember.getUser().getId(), groupMember.getOwnGuests()))
                            return false;
                    }else {
                        System.out.println("Error while changing member's guests");
                    }
                }
            }

            if (newGroupHead != null)
                groupDao.updateGroupHead(group.getId(), newGroupHead.getId());

            return true;

        }
        else
            System.out.println("Group is null during applyChanges");

        return false;
    }

    public ArrayList<Facility> getFacilitiesManaged() throws SQLException {
        return managesDAO.getAllFacilitiesByManager(person.getId());
    }


    }
