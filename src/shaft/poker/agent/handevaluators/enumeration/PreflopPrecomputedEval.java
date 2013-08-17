/*
 * The MIT License
 *
 * Copyright 2013 Thomas Schaffer <thomas.schaffer@epitech.eu>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package shaft.poker.agent.handevaluators.enumeration;

import java.util.ArrayList;
import java.util.List;
import shaft.poker.agent.IHandRange;
import shaft.poker.agent.enumerationtools.EnumCandidate;
import shaft.poker.agent.handevaluators.AHandEvaluator;
import shaft.poker.game.Card;
import shaft.poker.game.Card.Rank;
import shaft.poker.game.IHand;
import shaft.poker.game.IHand.HandType;
import shaft.poker.game.factory.ComponentFactory;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public class PreflopPrecomputedEval extends AHandEvaluator {
    
    // Pre-computed tables [via buildTab()] for raw hand strength, positive potential & negative potential
    // Dimensions [Suited 0/1][Rank card1][Rank card2]
    
    private static double[][][] _handStrTab = {
        {
            { 7.460254897324182E-4, 0.13227993676312272, 0.14101659476319928, 0.15566138215896003, 0.17648922305743467, 0.2024414453608464, 0.23584321323024127, 0.27623618861752586, 0.3239214224531796, 0.37935731755798174, 0.44270955091586656, 0.514308486017442, 0.5944809117561531 },
            { 0.13227993676312272, 0.6430741082132855, 0.2591397754437986, 0.27388190573383137, 0.293459744296846, 0.31639159675228545, 0.3443942845744491, 0.37902352594797467, 0.41937256902907205, 0.46585039702405506, 0.518577100346878, 0.5778425498600712, 0.6439392806807264 },
            { 0.14101659476319928, 0.2591397754437986, 0.6771031834122283, 0.28117309030103776, 0.30098573758188824, 0.32412949038474326, 0.3524708570045037, 0.3863307901604027, 0.4270065380668828, 0.47386263454517646, 0.5270191515031414, 0.5867659747993509, 0.6533956506423809 },
            { 0.15566138215896003, 0.27388190573383137, 0.28117309030103776, 0.7104697579145627, 0.30810175908196846, 0.33143597581665163, 0.36004767675687543, 0.39430087710815587, 0.4343905927179257, 0.48162030641264514, 0.5352021917208644, 0.5954261584939645, 0.6625847731232777 },
            { 0.17648922305743467, 0.293459744296846, 0.30098573758188824, 0.30810175908196846, 0.743160845472084, 0.33831486207716965, 0.3671735664391986, 0.4017567011011886, 0.44229208530044417, 0.48913326156801845, 0.5431362390344671, 0.6038334384867411, 0.6715174547829466 },
            { 0.2024414453608464, 0.31639159675228545, 0.32412949038474326, 0.33143597581665163, 0.33831486207716965, 0.7747622563666205, 0.37370488111305333, 0.4085280952875096, 0.449452332811943, 0.4967911119564787, 0.5506079938995606, 0.6117743115069518, 0.6799800518847698 },
            { 0.23584321323024127, 0.3443942845744491, 0.3524708570045037, 0.36004767675687543, 0.3671735664391986, 0.37370488111305333, 0.8056883962611401, 0.4149546599310265, 0.45618489524949635, 0.5039693944077175, 0.5583327765067294, 0.6195124784349101, 0.6882355349766417 },
            { 0.27623618861752586, 0.37902352594797467, 0.3863307901604027, 0.39430087710815587, 0.4017567011011886, 0.4085280952875096, 0.4149546599310265, 0.8359220498725664, 0.4626040486947391, 0.5107443260352258, 0.5656080861892061, 0.6273815264674306, 0.6963085107563183 },
            { 0.3239214224531796, 0.41937256902907205, 0.4270065380668828, 0.4343905927179257, 0.44229208530044417, 0.449452332811943, 0.45618489524949635, 0.4626040486947391, 0.86544751139326, 0.5172418139848856, 0.5725247326159284, 0.6348524759096309, 0.7044201484350536 },
            { 0.37935731755798174, 0.46585039702405506, 0.47386263454517646, 0.48162030641264514, 0.48913326156801845, 0.4967911119564787, 0.5039693944077175, 0.5107443260352258, 0.5172418139848856, 0.894093841947767, 0.5794357333796664, 0.6421218599749593, 0.7121575925744272 },
            { 0.44270955091586656, 0.518577100346878, 0.5270191515031414, 0.5352021917208644, 0.5431362390344671, 0.5506079938995606, 0.5583327765067294, 0.5656080861892061, 0.5725247326159284, 0.5794357333796664, 0.9220242061252308, 0.6493801221816089, 0.719822873889017 },
            { 0.514308486017442, 0.5778425498600712, 0.5867659747993509, 0.5954261584939645, 0.6038334384867411, 0.6117743115069518, 0.6195124784349101, 0.6273815264674306, 0.6348524759096309, 0.6421218599749593, 0.6493801221816089, 0.9492315970163917, 0.7274966412771487 },
            { 0.5944809117561531, 0.6439392806807264, 0.6533956506423809, 0.6625847731232777, 0.6715174547829466, 0.6799800518847698, 0.6882355349766417, 0.6963085107563183, 0.7044201484350536, 0.7121575925744272, 0.719822873889017, 0.7274966412771487, 0.9757146524018825 }
        },
        {
            { 0.0, 0.1273305067649667, 0.13324967536574017, 0.14499852226127669, 0.16300352695814824, 0.1863506340900975, 0.217477012826019, 0.2561253654098659, 0.302732767387851, 0.35784491096446713, 0.42174841633083726, 0.4948363192597637, 0.5774621358341314 },
            { 0.1273305067649667, 0.0, 0.2582428087241829, 0.2702662158218385, 0.2871075427119054, 0.30734764464246556, 0.33291326501312885, 0.36545233587565723, 0.40420360807322453, 0.44965121377946, 0.5020274376531325, 0.5616796357958771, 0.62892568694396 },
            { 0.13324967536574017, 0.2582428087241829, 0.0, 0.2772250364444793, 0.29432103058906794, 0.31480235679372814, 0.34075556998625756, 0.3726255602241709, 0.41177776887029593, 0.4576907488884709, 0.5105967529050301, 0.57084311194866, 0.6387476768541849 },
            { 0.14499852226127669, 0.2702662158218385, 0.2772250364444793, 0.0, 0.30099306379456825, 0.3216856613737489, 0.3479527731054737, 0.38028238616937793, 0.4189763461662159, 0.46535581489051997, 0.5187941990153633, 0.5796387999449056, 0.6482074363705772 },
            { 0.16300352695814824, 0.2871075427119054, 0.29432103058906794, 0.30099306379456825, 0.0, 0.32802138685120197, 0.35457113962664, 0.3872919438244042, 0.42651675565500685, 0.472660267575612, 0.5266328322301836, 0.5880789340315422, 0.6573163555795194 },
            { 0.1863506340900975, 0.30734764464246556, 0.31480235679372814, 0.3216856613737489, 0.32802138685120197, 0.0, 0.36049078755015584, 0.39350339830531483, 0.43319723810165034, 0.47994304864123444, 0.5339036646611349, 0.5959531504247698, 0.6658625786747148 },
            { 0.217477012826019, 0.33291326501312885, 0.34075556998625756, 0.3479527731054737, 0.35457113962664, 0.36049078755015584, 0.0, 0.3992806130773524, 0.4393519679315037, 0.48664304771072125, 0.5412747482486783, 0.6035295954302674, 0.6741121110889675 },
            { 0.2561253654098659, 0.36545233587565723, 0.3726255602241709, 0.38028238616937793, 0.3872919438244042, 0.39350339830531483, 0.3992806130773524, 0.0, 0.4451161534047978, 0.49285632035596855, 0.5481074570209268, 0.6111002228431105, 0.6820926228863995 },
            { 0.302732767387851, 0.40420360807322453, 0.41177776887029593, 0.4189763461662159, 0.42651675565500685, 0.43319723810165034, 0.4393519679315037, 0.4451161534047978, 0.0, 0.49872709502313584, 0.5545107707296426, 0.6181960265340857, 0.6899923113427959 },
            { 0.35784491096446713, 0.44965121377946, 0.4576907488884709, 0.46535581489051997, 0.472660267575612, 0.47994304864123444, 0.48664304771072125, 0.49285632035596855, 0.49872709502313584, 0.0, 0.5608714242888658, 0.6250596222431442, 0.697486652632243 },
            { 0.42174841633083726, 0.5020274376531325, 0.5105967529050301, 0.5187941990153633, 0.5266328322301836, 0.5339036646611349, 0.5412747482486783, 0.5481074570209268, 0.5545107707296426, 0.5608714242888658, 0.0, 0.6318411050807616, 0.704836467722647 },
            { 0.4948363192597637, 0.5616796357958771, 0.57084311194866, 0.5796387999449056, 0.5880789340315422, 0.5959531504247698, 0.6035295954302674, 0.6111002228431105, 0.6181960265340857, 0.6250596222431442, 0.6318411050807616, 0.0, 0.712127691482093 },
            { 0.5774621358341314, 0.62892568694396, 0.6387476768541849, 0.6482074363705772, 0.6573163555795194, 0.6658625786747148, 0.6741121110889675, 0.6820926228863995, 0.6899923113427959, 0.697486652632243, 0.704836467722647, 0.712127691482093, 0.0 }
        }
    };
    
    private static double[][][] _posPotTab = {
        {
            { 0.04256410696480289, 0.10397331930527161, 0.11134881942785231, 0.1184248394247434, 0.12612892489616778, 0.11815195660599041, 0.12804059506909166, 0.13498905326102278, 0.14429841664746382, 0.15147750945521712, 0.16083574329981026, 0.1734970107646318, 0.19199772429117934 },
            { 0.10397331930527161, 0.1306818520272486, 0.1840157962713492, 0.18999059425285142, 0.1961859433489775, 0.1855755979504077, 0.17997038322829767, 0.18873880397331874, 0.1953684173748927, 0.1983936401407666, 0.20236071206257014, 0.20759853140933043, 0.2150517057618416 },
            { 0.11134881942785231, 0.1840157962713492, 0.13418428342860267, 0.20778199731070668, 0.21442078288798058, 0.20390580661064778, 0.19836917496385367, 0.193100344708768, 0.2039919201959084, 0.20694793201265926, 0.21080199674605163, 0.2158785060487048, 0.2231268232998585 },
            { 0.1184248394247434, 0.18999059425285142, 0.20778199731070668, 0.1372535840200993, 0.2329056623390635, 0.2229097004740071, 0.21751700187531323, 0.21236701092180083, 0.20896899467448843, 0.21619106161292106, 0.22004917541466565, 0.22512874489304432, 0.23242047719666373 },
            { 0.12612892489616778, 0.1961859433489775, 0.21442078288798058, 0.2329056623390635, 0.13987672980556853, 0.24218401121161448, 0.2374047541344457, 0.23250949033780124, 0.22935771164841526, 0.22207284801748142, 0.23032852671027518, 0.23562410496725913, 0.2432885113239979 },
            { 0.11815195660599041, 0.1855755979504077, 0.20390580661064778, 0.2229097004740071, 0.24218401121161448, 0.14070529256088207, 0.2533455315012044, 0.24911907713695516, 0.2463768872935533, 0.23940864354974986, 0.23320291302383622, 0.24323441516791, 0.2517742600140096 },
            { 0.12804059506909166, 0.17997038322829767, 0.19836917496385367, 0.21751700187531323, 0.2374047541344457, 0.2533455315012044, 0.1417397932774284, 0.2661580728012464, 0.2644823914870163, 0.2578972097920756, 0.2522364408317139, 0.24803308312387531, 0.26184816770883684 },
            { 0.13498905326102278, 0.18873880397331874, 0.193100344708768, 0.21236701092180083, 0.23250949033780124, 0.24911907713695516, 0.2661580728012464, 0.14330284088496092, 0.2830592780131378, 0.27700403241175453, 0.2720965162756428, 0.2689595151641745, 0.2694444023070723 },
            { 0.14429841664746382, 0.1953684173748927, 0.2039919201959084, 0.20896899467448843, 0.22935771164841526, 0.2463768872935533, 0.2644823914870163, 0.2830592780131378, 0.14891316865851542, 0.3010720558124855, 0.2972409069496177, 0.29569279243703406, 0.2985965174109213 },
            { 0.15147750945521712, 0.1983936401407666, 0.20694793201265926, 0.21619106161292106, 0.22207284801748142, 0.23940864354974986, 0.2578972097920756, 0.27700403241175453, 0.3010720558124855, 0.1440869885239534, 0.3036891928637344, 0.3034037447110634, 0.3085743595749978 },
            { 0.16083574329981026, 0.20236071206257014, 0.21080199674605163, 0.22004917541466565, 0.23032852671027518, 0.23320291302383622, 0.2522364408317139, 0.2720965162756428, 0.2972409069496177, 0.3036891928637344, 0.1411300685603669, 0.312039064491822, 0.3198007912552343 },
            { 0.1734970107646318, 0.20759853140933043, 0.2158785060487048, 0.22512874489304432, 0.23562410496725913, 0.24323441516791, 0.24803308312387531, 0.2689595151641745, 0.29569279243703406, 0.3034037447110634, 0.312039064491822, 0.14297179686451203, 0.33204496839422437 },
            { 0.19199772429117934, 0.2150517057618416, 0.2231268232998585, 0.23242047719666373, 0.2432885113239979, 0.2517742600140096, 0.26184816770883684, 0.2694444023070723, 0.2985965174109213, 0.3085743595749978, 0.3198007912552343, 0.33204496839422437, 0.164852197080751 }
        },
        {
            { 0.0, 0.09832551202057796, 0.10812707337839857, 0.11762389451078392, 0.12768519664292516, 0.1226522852931999, 0.1350276546126751, 0.14428026933760463, 0.15548084022581898, 0.16476315675085346, 0.17599933347031585, 0.19019921473584944, 0.20963885566625745 },
            { 0.09832551202057796, 0.0, 0.1812484550337591, 0.18953139503844652, 0.19795114296479582, 0.19016036794123656, 0.18713499772812459, 0.19832020109965093, 0.20702629539872708, 0.21247572945427876, 0.2188003968824287, 0.2262870589885981, 0.2357599811529131 },
            { 0.10812707337839857, 0.1812484550337591, 0.0, 0.20791934626303457, 0.21673499799713578, 0.2089897241135439, 0.20596222661743185, 0.20309681646776634, 0.21598493295342786, 0.22132275245195732, 0.2274956695180949, 0.234789973270215, 0.24403840113930803 },
            { 0.11762389451078392, 0.18953139503844652, 0.20791934626303457, 0.0, 0.23589261563641967, 0.2286291419094505, 0.22566025820917937, 0.22281566239067843, 0.22144122993287915, 0.23094773558295636, 0.23708659940545962, 0.2443541057707503, 0.2536287427990355 },
            { 0.12768519664292516, 0.19795114296479582, 0.21673499799713578, 0.23589261563641967, 0.0, 0.24863962112336727, 0.24621507376574067, 0.2435200352130576, 0.24225973553148863, 0.2373570002451762, 0.24777555513423516, 0.25523006187414127, 0.2648629520781813 },
            { 0.1226522852931999, 0.19016036794123656, 0.2089897241135439, 0.2286291419094505, 0.24863962112336727, 0.0, 0.2632194059563374, 0.2611039488911609, 0.26012383029391245, 0.255391778727611, 0.2515179247461375, 0.2635652085052609, 0.27403391750737405 },
            { 0.1350276546126751, 0.18713499772812459, 0.20596222661743185, 0.22566025820917937, 0.24621507376574067, 0.2632194059563374, 0.0, 0.2792128219467193, 0.2791817801582418, 0.27471251669624003, 0.2712059764715064, 0.26928041275754516, 0.28488268495719443 },
            { 0.14428026933760463, 0.19832020109965093, 0.20309681646776634, 0.22281566239067843, 0.2435200352130576, 0.2611039488911609, 0.2792128219467193, 0.0, 0.29883355809686, 0.2948277181358273, 0.29193295813804565, 0.29085078399491715, 0.2934918738273015 },
            { 0.15548084022581898, 0.20702629539872708, 0.21598493295342786, 0.22144122993287915, 0.24225973553148863, 0.26012383029391245, 0.2791817801582418, 0.29883355809686, 0.0, 0.3199302547165829, 0.3180451358249131, 0.31835565431262197, 0.3230601822810798 },
            { 0.16476315675085346, 0.21247572945427876, 0.22132275245195732, 0.23094773558295636, 0.2373570002451762, 0.255391778727611, 0.27471251669624003, 0.2948277181358273, 0.3199302547165829, 0.0, 0.3258863891403447, 0.32742764937145474, 0.3343952939342988 },
            { 0.17599933347031585, 0.2188003968824287, 0.2274956695180949, 0.23708659940545962, 0.24777555513423516, 0.2515179247461375, 0.2712059764715064, 0.29193295813804565, 0.3180451358249131, 0.3258863891403447, 0.0, 0.33760004723645964, 0.34714649782008183 },
            { 0.19019921473584944, 0.2262870589885981, 0.234789973270215, 0.2443541057707503, 0.25523006187414127, 0.2635652085052609, 0.26928041275754516, 0.29085078399491715, 0.31835565431262197, 0.32742764937145474, 0.33760004723645964, 0.0, 0.36110391000947883 },
            { 0.20963885566625745, 0.2357599811529131, 0.24403840113930803, 0.2536287427990355, 0.2648629520781813, 0.27403391750737405, 0.28488268495719443, 0.2934918738273015, 0.3230601822810798, 0.3343952939342988, 0.34714649782008183, 0.36110391000947883, 0.0 }
        }
    };
    
    private static double[][][] _negPotTab = {
        {
            { 0.0072624942765287985, 0.34948469673975346, 0.33587224349557776, 0.3216386385587852, 0.30375300064258837, 0.2867158652831998, 0.26845433357361365, 0.25584955408846, 0.249018083763778, 0.24410188624256784, 0.24278087962792846, 0.24386873303782927, 0.246060431848868 },
            { 0.34948469673975346, 0.30916175651857797, 0.30055749206422355, 0.2925793933439789, 0.2810502019455485, 0.27010628067129633, 0.2604702229118892, 0.24741345371191784, 0.23862177966782158, 0.23170058966075416, 0.2278224158531351, 0.22630370431855884, 0.22608397083906753 },
            { 0.33587224349557776, 0.30055749206422355, 0.2785085755886412, 0.28615242748022024, 0.2753870344647096, 0.2639136880513169, 0.2534317086670829, 0.2455745923751276, 0.23570184532166555, 0.2286412199659974, 0.2246192296614763, 0.2229658241142422, 0.222634200486189 },
            { 0.3216386385587852, 0.2925793933439789, 0.28615242748022024, 0.2501869971506914, 0.267680688881342, 0.2567642773516796, 0.24597524296735046, 0.23723661243559163, 0.23254098267914422, 0.2244024841980852, 0.22037572347338666, 0.21871406550808373, 0.21838464671750255 },
            { 0.30375300064258837, 0.2810502019455485, 0.2753870344647096, 0.267680688881342, 0.2239205595819438, 0.2474957460598053, 0.23742065785599864, 0.22838961901349866, 0.22281759202339546, 0.21989097054022025, 0.2148209417317201, 0.2133019464398043, 0.21311080634579735 },
            { 0.2867158652831998, 0.27010628067129633, 0.2639136880513169, 0.2567642773516796, 0.2474957460598053, 0.20249566604779887, 0.22643465041232022, 0.21907495270314195, 0.21371934578658644, 0.21020921635993067, 0.21052629365225914, 0.20816360199189257, 0.20836496391189135 },
            { 0.26845433357361365, 0.2604702229118892, 0.2534317086670829, 0.24597524296735046, 0.23742065785599864, 0.22643465041232022, 0.1824537186788719, 0.20893350096266605, 0.20482122892763086, 0.20141237394063685, 0.20107581520644963, 0.20395368389818921, 0.20328670597195744 },
            { 0.25584955408846, 0.24741345371191784, 0.2455745923751276, 0.23723661243559163, 0.22838961901349866, 0.21907495270314195, 0.20893350096266605, 0.16363785736181144, 0.19559903879022883, 0.19328360147108664, 0.1928810271564822, 0.19502513743984212, 0.1993497565920673 },
            { 0.249018083763778, 0.23862177966782158, 0.23570184532166555, 0.23254098267914422, 0.22281759202339546, 0.21371934578658644, 0.20482122892763086, 0.19559903879022883, 0.14501374335202394, 0.18387230014969844, 0.184534154628132, 0.18638244422199798, 0.18972116654724638 },
            { 0.24410188624256784, 0.23170058966075416, 0.2286412199659974, 0.2244024841980852, 0.21989097054022025, 0.21020921635993067, 0.20141237394063685, 0.19328360147108664, 0.18387230014969844, 0.13140779411385475, 0.17998720787951875, 0.18277675772490992, 0.18660944329402573 },
            { 0.24278087962792846, 0.2278224158531351, 0.2246192296614763, 0.22037572347338666, 0.2148209417317201, 0.21052629365225914, 0.20107581520644963, 0.1928810271564822, 0.184534154628132, 0.17998720787951875, 0.11846117729752541, 0.1788415086687143, 0.18356757867812734 },
            { 0.24386873303782927, 0.22630370431855884, 0.2229658241142422, 0.21871406550808373, 0.2133019464398043, 0.20816360199189257, 0.20395368389818921, 0.19502513743984212, 0.18638244422199798, 0.18277675772490992, 0.1788415086687143, 0.10590418611024517, 0.18057101362976247 },
            { 0.246060431848868, 0.22608397083906753, 0.222634200486189, 0.21838464671750255, 0.21311080634579735, 0.20836496391189135, 0.20328670597195744, 0.1993497565920673, 0.18972116654724638, 0.18660944329402573, 0.18356757867812734, 0.18057101362976247, 0.09344132177347857 }
        },
        {
            { 0.0, 0.37578979955199154, 0.36336614827805774, 0.3482934149569794, 0.3279422729604262, 0.30795551590241566, 0.28661143806890543, 0.2712236842064095, 0.26218443169383493, 0.2559575455245975, 0.25383132299506117, 0.2543168704578463, 0.2558637802975665 },
            { 0.37578979955199154, 0.0, 0.3177145589568393, 0.31015649203386353, 0.2983437247352794, 0.2864866013470055, 0.27570937339797635, 0.2614310777425789, 0.25127156418365537, 0.243401004356952, 0.2388313790978786, 0.23675872212722804, 0.23596545104018793 },
            { 0.36336614827805774, 0.3177145589568393, 0.0, 0.304152094236966, 0.2931887240974272, 0.28070353982132734, 0.26884347898334937, 0.2596718807499977, 0.24846930189042857, 0.240442151109646, 0.23571872424764106, 0.23350946776845913, 0.23261134591025698 },
            { 0.3482934149569794, 0.31015649203386353, 0.304152094236966, 0.0, 0.2857741973682992, 0.27379763817941993, 0.2615119359881628, 0.25121534678989266, 0.24522148460371188, 0.23614914950601065, 0.2314332413890961, 0.22923518915523636, 0.22836544652905363 },
            { 0.3279422729604262, 0.2983437247352794, 0.2931887240974272, 0.2857741973682992, 0.0, 0.2645277276733515, 0.2529149400219112, 0.24221163206508, 0.23511734601523585, 0.23139917274081584, 0.2256834434985007, 0.2236728538076052, 0.22299055790026026 },
            { 0.30795551590241566, 0.2864866013470055, 0.28070353982132734, 0.27379763817941993, 0.2645277276733515, 0.0, 0.2415609541217474, 0.23248930570396606, 0.22550534864331956, 0.22099542559005358, 0.22089650081902706, 0.21809091204093553, 0.21786469048025886 },
            { 0.28661143806890543, 0.27570937339797635, 0.26884347898334937, 0.2615119359881628, 0.2529149400219112, 0.2415609541217474, 0.0, 0.2218947953930131, 0.2161551710485984, 0.21165145105635272, 0.21070945855309964, 0.21343375374817616, 0.21238616328135287 },
            { 0.2712236842064095, 0.2614310777425789, 0.2596718807499977, 0.25121534678989266, 0.24221163206508, 0.23248930570396606, 0.2218947953930131, 0.0, 0.20645514103684823, 0.20306181504172338, 0.20196337937574843, 0.20377721069411955, 0.2080486730621567 },
            { 0.26218443169383493, 0.25127156418365537, 0.24846930189042857, 0.24522148460371188, 0.23511734601523585, 0.22550534864331956, 0.2161551710485984, 0.20645514103684823, 0.0, 0.19326652209082654, 0.1932840936749037, 0.19471482121960498, 0.19780655451717008 },
            { 0.2559575455245975, 0.243401004356952, 0.240442151109646, 0.23614914950601065, 0.23139917274081584, 0.22099542559005358, 0.21165145105635272, 0.20306181504172338, 0.19326652209082654, 0.0, 0.18823965728791234, 0.19061470930168994, 0.1942194831420147 },
            { 0.25383132299506117, 0.2388313790978786, 0.23571872424764106, 0.2314332413890961, 0.2256834434985007, 0.22089650081902706, 0.21070945855309964, 0.20196337937574843, 0.1932840936749037, 0.18823965728791234, 0.0, 0.18624217612722732, 0.19074389745991088 },
            { 0.2543168704578463, 0.23675872212722804, 0.23350946776845913, 0.22923518915523636, 0.2236728538076052, 0.21809091204093553, 0.21343375374817616, 0.20377721069411955, 0.19471482121960498, 0.19061470930168994, 0.18624217612722732, 0.0, 0.1873628818697496 },
            { 0.2558637802975665, 0.23596545104018793, 0.23261134591025698, 0.22836544652905363, 0.22299055790026026, 0.21786469048025886, 0.21238616328135287, 0.2080486730621567, 0.19780655451717008, 0.1942194831420147, 0.19074389745991088, 0.1873628818697496, 0.0 }
        }
    };
    
    @Override
    public void compute(List<Card> holecards, List<Card> board, IHandRange range, int numPlayers) {
        Card c1 = holecards.get(0);
        Card c2 = holecards.get(1);
        
        _handStr = _handStrTab[c1.suit() == c2.suit() ? 1 : 0][c1.rank().ordinal()][c2.rank().ordinal()];
        _posPot = _posPotTab[c1.suit() == c2.suit() ? 1 : 0][c1.rank().ordinal()][c2.rank().ordinal()];
        _negPot = _negPotTab[c1.suit() == c2.suit() ? 1 : 0][c1.rank().ordinal()][c2.rank().ordinal()];
        _handStr = Math.pow(_handStr, numPlayers - 1);
    }


    public static void buildTab() {
        
        int count = 0;
        
        double[][][] _handStr = new double[2][Rank.values().length][Rank.values().length];
        double[][][] _posPot = new double[2][Rank.values().length][Rank.values().length];
        double[][][] _negPot = new double[2][Rank.values().length][Rank.values().length];
        
        double[][] HP = new double[3][3];
        double[] HPTotal = new double[3];
        
        List<Card> holeCards = new ArrayList<>(2);
        List<Card> board = new ArrayList<>(5);
        List<Card> oppHole = new ArrayList<>(2);
        
        List<EnumCandidate> holeCandidates = EnumCandidate.buildCandidates(holeCards, board);
        
        for (EnumCandidate cand : holeCandidates) {
            for (int i = 0; i < HP.length; i++) {
                for (int j = 0; j < HP[i].length; j++) {
                    HP[i][j] = 0;
                }
            }
            
            for (int i = 0; i < HPTotal.length; i++) {
                HPTotal[i] = 0;
            }
            
            List<Card> flop1Cand = new ArrayList<>(Card.values());
            flop1Cand.removeAll(cand.cards());
            List<Card> flop2Cand = new ArrayList<>(flop1Cand);
            
            
            for (Card flop1 : flop1Cand) {
                    board.add(flop1);
                    flop2Cand.remove(flop1);
                    
                    List<Card> flop3Cand = new ArrayList<>(flop2Cand);
                    
                    for (Card flop2 : flop2Cand) {
                            board.add(flop2);
                            flop3Cand.remove(flop2);
                            
                            List<Card> turnCand = new ArrayList<>(flop3Cand);
                            
                            for (Card flop3 : flop3Cand) {
                                    board.add(flop3);
                                    turnCand.remove(flop3);
                                    
                                    IHand currentHand = ComponentFactory.buildHand(cand.cards(), board);
                                    
                                    List<EnumCandidate> oppCandidates = EnumCandidate.buildCandidates(cand.cards(), board);
                                    
                                    for (EnumCandidate oppCand : oppCandidates) {
                                        IHand oppHand = ComponentFactory.buildHand(oppCand.cards(), board);
                                        int idx = currentHand.compareTo(oppHand) + 1;

                                        
                                        if (idx > 0 && (currentHand.type() == HandType.STRAIGHT || currentHand.type() == HandType.FLUSH
                                                || currentHand.type() == HandType.STRAIGHT_FLUSH || currentHand.type() == HandType.ROYAL_FLUSH)) {
                                            
                                            HP[0][2] += 1 * cand.weight() * oppCand.weight();
                                            HPTotal[0] += 1 * cand.weight() * oppCand.weight();
                                        }
                                        else {
                                                                                    
                                            List<Card> newTurnCand = new ArrayList<>(turnCand);
                                            newTurnCand.removeAll(oppCand.cards());
                                            List<Card> riverCand = new ArrayList<>(newTurnCand);
                                        
                                            for (Card turn : newTurnCand) {
                                                    board.add(turn);
                                                    riverCand.remove(turn);
                                                    
                                                    for (Card river : riverCand) {
                                                            board.add(river);
                                                            
                                                            IHand futureOwnHand = ComponentFactory.buildHand(cand.cards(), board);
                                                            IHand futureOppHand = ComponentFactory.buildHand(oppCand.cards(), board);
                                                            int futIdx = futureOwnHand.compareTo(futureOppHand) + 1;
                                                            
                                                            HP[idx][futIdx] += 1 * cand.weight() * oppCand.weight();
                                                            HPTotal[idx] += 1 * cand.weight() * oppCand.weight();
                                                            
                                                            board.remove(river);
                                                    }
                                                    board.remove(turn);
                                            }
                                        }
                                        
                                    }
                                    board.remove(flop3);
                            }
                            board.remove(flop2);
                    }
                    board.remove(flop1);
                
            }
            
            double posPot = (HP[0][2] + HP[0][1] / 2 + HP[1][2] / 2) / (HPTotal[0] + HPTotal[1] / 2);
            double negPot = (HP[2][0] + HP[1][0] / 2 + HP[2][1] / 2) / (HPTotal[2] + HPTotal[1] / 2);
            double handStr = (HPTotal[2] + HPTotal[1] / 2) / (HPTotal[0] + HPTotal[1] + HPTotal[2]);
            
            int idx1 = cand.cards().get(0).suit() == cand.cards().get(1).suit() ? 1 : 0;
            int idx2 = cand.cards().get(0).rank().ordinal();
            int idx3 = cand.cards().get(1).rank().ordinal();
            
            _handStr[idx1][idx2][idx3] = handStr;
            _posPot[idx1][idx2][idx3] = posPot;
            _negPot[idx1][idx2][idx3] = negPot;
            _handStr[idx1][idx3][idx2] = handStr;
            _posPot[idx1][idx3][idx2] = posPot;
            _negPot[idx1][idx3][idx2] = negPot;
            
            System.out.println(++count);
        }
        
        prettyPrintTab(_handStr, "_handStr");
        prettyPrintTab(_posPot, "_posPot");
        prettyPrintTab(_negPot, "_negPot");
        
    }
        
    private static void prettyPrintTab(double[][][] tab, String name) {
        System.out.println(name + " = {");
        for (int i = 0; i < tab.length; i++) {
            System.out.println("{");
            
            for (int j = 0; j < tab[i].length; j++) {
                System.out.print("{ ");
                
                for (int k = 0; k < tab[i][j].length; k++) {
                    System.out.print(tab[i][j][k]);
                    if (k < tab[i][j].length - 1) {
                        System.out.print(", ");
                    }
                }
                
                System.out.print(" }");
                if (j < tab[i].length - 1) {
                    System.out.print(",");
                }
                System.out.println();
            }
            
            System.out.print("}");
            if (i < tab.length - 1) {
                System.out.print(",");
            }
            System.out.println();
        }
        System.out.println("};");
    }
    
}