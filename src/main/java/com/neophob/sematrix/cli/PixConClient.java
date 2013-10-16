/**
 * Copyright (C) 2011-2013 Michael Vogt <michu@neophob.com>
 *
 * This file is part of PixelController.
 *
 * PixelController is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PixelController is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PixelController.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.neophob.sematrix.cli;

import jargs.gnu.CmdLineParser;
import jargs.gnu.CmdLineParser.IllegalOptionValueException;
import jargs.gnu.CmdLineParser.UnknownOptionException;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.commons.lang3.ArrayUtils;

import processing.core.PApplet;
import processing.net.Client;

import com.neophob.sematrix.listener.TcpServer;
import com.neophob.sematrix.properties.CommandGroup;
import com.neophob.sematrix.properties.ValidCommands;

/**
 * 
 * @author michu
 *
 */
public class PixConClient {

	private static final float VERSION = 0.5f; 
	
    /** The Constant DEFAULT_PORT. */
    private static final int DEFAULT_PORT = 3448;
    private static final int DEFAULT_JMX_PORT = 1337;

    /** The Constant DEFAULT_HOST. */
    private static final String DEFAULT_HOST = "127.0.0.1";

    private static final String PARAM_COMMAND = "command";
    private static final String PARAM_PORT = "port";
    private static final String PARAM_HOST = "hostname";

    protected PixConClient() {
    	//Util class
    }
    
    /**
     * 
     * @param s
     * @param length
     * @return
     */
    private static String pretifyString(String s, int length) {
        StringBuffer sb = new StringBuffer();
        
        sb.append(s);
        while (sb.length()<length) {
            sb.append(' ');
        }
        
        return sb.toString();
    }
    
    /**
     * Usage.
     *
     * @param options the options
     */
    private static void usage() {
        System.out.println("Usage: Client [-h hostname] [-p port] -c ValidCommand");
        System.out.println("Valid commands:");
 
        for (CommandGroup cg: CommandGroup.values()) {
            for (ValidCommands vc: ValidCommands.getCommandsByGroup(cg)) {
            	System.out.println("\t"
            	            +pretifyString(vc.toString(),28)
            	            +pretifyString("# of parameters: "+vc.getNrOfParams(), 23)
            	            +vc.getDesc());
            }
            System.out.println();
        }
    }


    /**
     * Parses the argument.
     *
     * @param args the args
     * @return the parsed argument
     * @throws InvalidParameterException 
     */
    protected static ParsedArgument parseArgument(String[] args) throws InvalidParameterException {

        if (args.length<2) {
            System.out.println("No arguments specified!\n");
            throw new InvalidParameterException("No arguments specified!");
        }

        CmdLineParser parser = new CmdLineParser();
        CmdLineParser.Option host = parser.addStringOption('h', PARAM_HOST);
        CmdLineParser.Option port = parser.addIntegerOption('p', PARAM_PORT);
        CmdLineParser.Option command = parser.addStringOption('c', PARAM_COMMAND);

        String pCmd = "undefined";
        try {
            parser.parse(args);
            String pHost = (String)parser.getOptionValue(host, DEFAULT_HOST);
            int pPort = (Integer)parser.getOptionValue(port, DEFAULT_PORT);
            pCmd = (String)parser.getOptionValue(command);        
            if (pCmd==null) {
                System.out.println("Unknown Command: "+ArrayUtils.toString(args));
                System.out.println("Exit now");
                throw new IllegalArgumentException("no ValidCommand specified!");
            }
            
            ValidCommands parsedCommand = ValidCommands.valueOf(pCmd.toUpperCase());
            String[] otherArgs = parser.getRemainingArgs();
            
            if (parsedCommand.getNrOfParams() < otherArgs.length) {
            	String err = "Invalid parameter count, expected: "+parsedCommand.getNrOfParams()+", provided: "+otherArgs.length;
            	System.out.println(err);
            	throw new InvalidParameterException(err);
            }
            
            String param = "";
            for (String s: otherArgs) {
                param += s+" ";
            }

            return new ParsedArgument(pHost, pPort, parsedCommand, param.trim());
        } catch (UnknownOptionException e) {
        	System.out.println("Invalid option defined: "+e.getMessage());
        	System.out.println();
        } catch (IllegalOptionValueException e) {
        	System.out.println("Invalid option value defined: "+e.getMessage());
        	System.out.println();
        } catch (IllegalArgumentException e) {
			System.out.println("Invalid command defined <"+pCmd+">: "+e.getMessage());
			System.out.println();
		}

        throw new InvalidParameterException("Something went wrong..");
    }

    /**
     * 
     * @param cmd
     * @return
     * @throws ConnectException
     */
    private static Client connectToServer(ParsedArgument cmd) throws ConnectException {
        Client client = null;
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(cmd.getHostname(), cmd.getPort()), 2000);
            client = new Client(new PApplet(), socket);
            System.out.println("Connected to "+cmd.getTarget());
            return client;
        } catch (Exception e) {
            System.err.println("Failed to connect Server at "+cmd.getTarget());
            if (socket!=null) {
                try {
                    socket.close();
                } catch (Exception e2) {}
            }
        }       
        throw new ConnectException("Failed to connect "+cmd.getTarget());
    }

    /**
     * The main method.
     *
     * @param args the arguments
     * @throws ParseException the parse exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println("PixelController Client v"+VERSION+"\n");

        ParsedArgument cmd=null;        
        try {
            cmd = parseArgument(args);
        } catch (Exception e) {  
        	usage();
            System.exit(1);            
        }
        
        System.out.println("\t"+cmd);
        
        if (cmd.getCommand() == ValidCommands.JMX_STAT) {
            int port = cmd.getPort();
            if (port==DEFAULT_PORT) {
                System.out.println("No Port specified, using default JMX port "+DEFAULT_JMX_PORT);   
                port = DEFAULT_JMX_PORT;
            }            
        	PixConClientJmx.queryJmxServer(cmd.getHostname(), port);
        } else {
            Client c = connectToServer(cmd);       
            c.write(cmd.getPayload(TcpServer.FUDI_MSG_END_MARKER));
            
            System.out.println("Close connection, Bye!");
            c.dispose();        	
        }
    }
}
