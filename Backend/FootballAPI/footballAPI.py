import requests
import json
import os
import pandas as pd
import kagglehub

API_KEY_PATHWAY = "apiKey.txt"
TEAM_FILE_PATHWAY = "../../allTeams.txt"
PLAYER_FILE_PATHWAY = "../../allPlayers.txt"
TEAM_LOGOS_PATHWAY = "../../teamLogos"
FIXTURE_FILE_PATHWAY = "../../allFixtures.txt"
URL = "https://v3.football.api-sports.io"
PREMIER_LEAGUE_ID_API = 39
PREMIER_LEAGUE_ID_FIFA = 13
LEAGUE_NAME = "Premier League"

EARLY_FIFA_DATABASE_LINK_START = "stefanoleone992/fifa-" # + fifa version (last 2 digits of season end year)
EARLY_FIFA_DATABASE_LINK_END = "-complete-player-dataset"

#links are slightly different each time
KAGGLE_DATABASE_LINKS = {
    20 : ("stefanoleone992/fifa-", "-complete-player-dataset"),
    21 : ("stefanoleone992/fifa-", "-complete-player-dataset"),
    22 : ("stefanoleone992/fifa-", "-complete-player-dataset"),
    23 : ("stefanoleone992/fifa-", "-complete-player-dataset"),
    24 : ("stefanoleone992/ea-sports-fc-", "-complete-player-dataset"),
    25 : ("nyagami/ea-sports-fc-", "-database-ratings-and-stats")
}


PLAYER_ATTRIBUTES = ["player_id", "short_name", "club_name", "player_positions", "club_position", "overall", "pace", "shooting", "passing", "dribbling", "defending", "physic"]

api_key = "fb5d179556e47eac911953572e8fc472" #passed from apiWindow.java -> or if already exists, from apiKey.txt
season_start_year = "2023" #passed from settings.java -> default to 2023/2024 season
season_end_year = "2024"
fifa_version = 24 #last 2 digits of end year

#read the current data's season start year from season.txt
season_file = open("season.txt", "r")
season_start_year = season_file.readline().strip()
season_end_year = str(int(season_start_year) + 1)
fifa_version = int(season_end_year[-2:])

print(season_start_year)
print(season_end_year)
print(fifa_version)

TEAM_NAME_LOOKUP = {
    "Arsenal": "Arsenal",
    "Aston Villa": "Aston Villa",
    "Bournemouth": "AFC Bournemouth",
    "Barnsley": "Barnsley",
    "Birmingham": "Birmingham City",
    "Blackburn": "Blackburn Rovers",
    "Blackpool": "Blackpool",
    "Bolton": "Bolton Wanderers",
    "Brentford": "Brentford",
    "Brighton": "Brighton & Hove Albion",
    "Burnley": "Burnley",
    "Cardiff": "Cardiff City",
    "Charlton": "Charlton Athletic",
    "Chelsea": "Chelsea",
    "Coventry": "Coventry City",
    "Crystal Palace": "Crystal Palace",
    "Derby": "Derby County",
    "Everton": "Everton",
    "Fulham": "Fulham",
    "Huddersfield": "Huddersfield Town",
    "Hull": "Hull City",
    "Ipswich": "Ipswich Town",
    "Leeds": "Leeds United",
    "Leicester": "Leicester City",
    "Liverpool": "Liverpool",
    "Luton": "Luton Town",
    "Man City": "Manchester City",
    "Man United": "Manchester United",
    "Middlesbrough": "Middlesbrough",
    "Newcastle": "Newcastle United",
    "Norwich": "Norwich City",
    "Nottingham Forest": "Nottingham Forest",
    "Oldham": "Oldham Athletic",   # pre-2004 but never returned
    "Portsmouth": "Portsmouth",
    "QPR": "Queens Park Rangers",
    "Reading": "Reading",
    "Sheffield Utd": "Sheffield United",
    "Sheffield Wed": "Sheffield Wednesday",
    "Southampton": "Southampton",
    "Stoke": "Stoke City",
    "Sunderland": "Sunderland",
    "Swansea": "Swansea City",
    "Tottenham": "Tottenham Hotspur",
    "Watford": "Watford",
    "West Brom": "West Bromwich Albion",
    "West Ham": "West Ham United",
    "Wigan": "Wigan Athletic",
    "Wolves": "Wolverhampton Wanderers"
}

def lookup_team_name(name):
    if TEAM_NAME_LOOKUP.get(name) is not None:
        return TEAM_NAME_LOOKUP[name]
    return name

#getting a json string of all fixtures from a given season
def get_fixtures(leagueID, season):
    query_string = {
        "league" : str(leagueID),
        "season" : season
    }

    headers = {
    'x-rapidapi-key': api_key,
    'x-rapidapi-host': 'v3.football.api-sports.io'
    }

    url = URL + "/fixtures"

    response = requests.get(url, headers=headers, params=query_string)
    data = response.json()

    return data


#get a json string of all teams in a league from one season
def get_teams(leagueID, season):
    query_string = {
        "league" : str(leagueID),
        "season" : season
    }

    headers = {
    'x-rapidapi-key': api_key,
    'x-rapidapi-host': 'v3.football.api-sports.io'
    }

    url = URL + "/teams"

    response = requests.get(url, headers=headers, params=query_string)
    data = response.json()

    return data


#download team logos from apifootball.com
def download_team_logos(team_JSON, folder=TEAM_LOGOS_PATHWAY):
    print("downloading team logos")
    os.makedirs(folder, exist_ok=True)

    for team in team_JSON["response"]:
        team = team["team"]
        team_name = lookup_team_name(team["name"])
        logo_URL = team["logo"]

        print(team_name, logo_URL)

        file_name = team_name.replace(" ", "_").replace("/", "_")
        filePath = os.path.join(folder, f"{file_name}.png")

        if os.path.exists(filePath):
            continue
            
        try:
            r = requests.get(logo_URL, timeout=20)
            if r.status_code == 200:
                with open(filePath, "wb") as f:
                    f.write(r.content)
                print(f"✅ Downloaded {team_name}")
            else:
                print(f"⚠️ Failed {team_name} ({r.status_code})")
        except Exception as e:
            print(f"❌ Error downloading {team_name}: {e}")


def write_teams_to_file(tuples):
    file = open(TEAM_FILE_PATHWAY, "w")
    file.write(season_start_year + "\n")
    i = 0

    for tuple in tuples:
        file.write(tuple[0] + "\t" + tuple[1] + "\t\n")
        i += 1

    file.close()


def write_fixtures_to_file(fixtures):
    file = open(FIXTURE_FILE_PATHWAY, "w")
    file.write(season_start_year + "\n")

    for key in fixtures:
        for tuple in fixtures[key]:
            file.write(tuple[0] + "," + tuple[1] + "\n")

    file.close()


def get_all_players_from_league(df, league_id, season):
    #drop any players without a league
    df = df.dropna(subset=["league_id"])
    #convert all float entries to integers
    df["fifa_version"] = df["fifa_version"].astype(int)
    df["league_id"] = df["league_id"].astype(int)
    #return the dataframe
    return df[(df["league_id"] == league_id) & (df["fifa_version"] == season)]


data_exists = True
should_read_data = False

data_exists = os.path.exists(TEAM_FILE_PATHWAY)

if(data_exists):
    file = open(TEAM_FILE_PATHWAY, "r")
    current_data_season = file.readline().strip()
    print("CURRENT DATA SEASON: " + str(current_data_season))
    data_exists = current_data_season == season_start_year
    should_read_data = not data_exists
else:
    data_exists = False
    should_read_data = True



if(should_read_data):
    print("data season is different, read data")

    #get fixture and team json strings
    fixture_JSON = get_fixtures(PREMIER_LEAGUE_ID_API, season_start_year)
    team_JSON = get_teams(PREMIER_LEAGUE_ID_API, season_start_year)

    print(json.dumps(team_JSON["response"], indent=2))

    all_teams = []

    #get all teams from the season and their abbreviations
    for team in team_JSON["response"]:
        #get team section of data
        teamData = team["team"]
        #get name and code of team
        name = lookup_team_name(teamData["name"])
        
        code = teamData["code"]
        #create and append tuple
        teamTuple = (name, code)
        all_teams.append(teamTuple)

    for tuple in all_teams:
        print(tuple)

    #download all team logos into ../../teamLogos
    download_team_logos(team_JSON)

    all_fixtures = {}

    #get all fixtures in order from the given season and league
    for fixture in fixture_JSON["response"]:
        if fixture["league"]["id"] == int(PREMIER_LEAGUE_ID_API) and fixture["fixture"]["status"]["short"] != "PST":
            round_num = fixture["league"]["round"].replace("Regular Season - ", "")

            teams = fixture["teams"]
            home = teams["home"]["name"]
            away = teams["away"]["name"]

            home_team = lookup_team_name(home)
            away_team = lookup_team_name(away)

            if all_fixtures.get(round_num) == None:
                all_fixtures[round_num] = []
            all_fixtures[round_num].append((home_team, away_team))


    #write all teams to external file
    write_teams_to_file(all_teams)

    #write all fixtures to external file
    write_fixtures_to_file(all_fixtures)



    #create link to download database from
    link_tuple = KAGGLE_DATABASE_LINKS[fifa_version]
    database_link = link_tuple[0] + str(fifa_version) + link_tuple[1]
    print(database_link)

    #now download the correct fifa database
    path = kagglehub.dataset_download(database_link)
    print(path)

    #get the dataframe of all male players
    player_DF = pd.read_csv(path + "/male_players.csv")
    attributes = player_DF.columns.to_list()
    print(attributes)

    #get all players from the premier league
    all_fifa_players = get_all_players_from_league(player_DF, PREMIER_LEAGUE_ID_FIFA, fifa_version)
    #get all fifa players and drop any duplicates
    league_player_data = all_fifa_players[PLAYER_ATTRIBUTES].drop_duplicates(subset=["player_id"], keep="first")

    #now write all of this to external file
    league_player_data.to_csv(PLAYER_FILE_PATHWAY, index=False, sep="\t")
else:
    print("no need to read new data, all the data already exists")

print("complete")