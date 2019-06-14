package it.polito.tdp.meteo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.sql.Date;
import it.polito.tdp.meteo.bean.Rilevamento;

public class MeteoDAO {

	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {

		return null;
	}

	public Double getAvgRilevamentiLocalitaMese(int mese, String localita) {

		return 0.0;
	}

	public HashSet<Integer> getMesi() {
		
		final String sql="SELECT DISTINCT MONTH(Data) " + 
				"FROM situazione s " + 
				"ORDER BY MONTH(Data)";
		HashSet<Integer> mesi= new HashSet<Integer>();
				
		try {
			Connection conn= DBConnect.getInstance().getConnection();
			PreparedStatement st= conn.prepareStatement(sql);
			ResultSet rs= st.executeQuery();
			
			while(rs.next()) {
				Integer i= rs.getInt(1);
				
				mesi.add(i);
			}
			conn.close();
			
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
		return mesi;
	}

	public String getUmiditaMedia(int mese) {
		final String sql="SELECT Localita, AVG(Umidita) " + 
				"FROM situazione s " + 
				"WHERE MONTH(Data)= ? " + 
				"GROUP BY Localita";
		
		String umiditaMedia= "";
		try {
			Connection conn= DBConnect.getInstance().getConnection();
			PreparedStatement st= conn.prepareStatement(sql);
			st.setInt(1, mese);
			ResultSet rs= st.executeQuery();
			while(rs.next()) {
				umiditaMedia+= String.format("%s, %s\n", rs.getString(1), rs.getString(2));
			}
			
			conn.close();
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
		return umiditaMedia;
	}

}
